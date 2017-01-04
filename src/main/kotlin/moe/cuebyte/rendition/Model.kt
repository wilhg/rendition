package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.Connection
import moe.cuebyte.rendition.Util.genField
import moe.cuebyte.rendition.Util.genId
import java.util.*

abstract class Model(val name: String) {

  /**
   * This property can only be modified before the Model instance was used.
   */
  val schema: MutableMap<String, Column> = HashMap()

  private var initialized: Boolean = false
  internal lateinit var pk: Column
  internal val columnSet: MutableSet<Column> = HashSet()
  internal val indexSet: MutableSet<Column> = HashSet()

  internal fun initialize() {
    if (initialized) return
    if (schema.isEmpty()) throw Exception("Columns should be initialized first.")

    schema.forEach {
      val col = it.value
      col.name = it.key

      columnSet.add(col)
      if (col.isPk) {
        if (col.type == Boolean::class.java) throw Exception("Primary key type error.")
        if (col.type != String::class.java) {
          indexSet.add(col)
          col.automated = false
        }
      }
      if (col.isIndex) indexSet.add(col)
    }

    initialized = true
  }

  /**
   * Use Transaction
   * @return id value in string if succeed, else return null
   */
  fun insert(fill: (InsertData)->Unit): String? {
    initialize()
    val input = InsertData(this)
    fill(input)
    input.init()

    val id = input.id
    // --- BEGIN Transaction ---
    val t = Connection.get().multi()
    t.hmset(genId(this, id), input.encodeData())
    input.stringIndices.forEach {
      t.sadd(genField(this, it.key, it.value), id)
    }
    input.doubleIndices.forEach {
      t.zadd(genField(this, it.key), mutableMapOf(id to it.value))
    }
    return if (t.exec().isEmpty()) null else id
    // --- END Transaction ---
  }

  protected fun bool(default: Boolean = false) = Column(Boolean::class.java, default)
  protected fun string(default: String = "") = Column(String::class.java, default)
  protected fun int(default: Int = 0) = Column(Int::class.java, default)
  protected fun long(default: Long = 0) = Column(Long::class.java, default)
  protected fun float(default: Float = 0f) = Column(Float::class.java, default)
  protected fun double(default: Double = 0.0) = Column(Double::class.java, default)
}