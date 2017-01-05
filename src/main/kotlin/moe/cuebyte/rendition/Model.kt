package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.Connection
import moe.cuebyte.rendition.Util.genId
import moe.cuebyte.rendition.Util.genKey
import java.util.*

abstract class Model(val name: String) {

  /**
   * This property can only be modified before the Model instance was used.
   */
  val schema: MutableMap<String, Column> = HashMap()

  internal lateinit var pk: Column
  internal val columnSet: MutableSet<Column> = HashSet()
  internal val indexSet: MutableSet<Column> = HashSet()
  private var initialized: Boolean = false

  internal fun initialize() {
    if (initialized) return
    if (schema.isEmpty()) {
      throw Exception("Columns should be initialized first.")
    }

    schema.forEach {
      val col = it.value
      col.name = it.key

      columnSet.add(col)
      if (col.isIndex) indexSet.add(col)
      if (col.isPk) {
        if (col.type == Boolean::class.java) {
          throw Exception("Primary key can not defined as Boolean")
        }
        if (col.type != String::class.java) {
          indexSet.add(col)
          col.automated = false
        }
      }
    }
    initialized = true
  }

  /**
   * Use Transaction
   * @return id value in string if succeed, else return null
   */
  fun insert(body: (InsertData)->Unit): String? {
    initialize()
    val input = InsertData(this)
    body(input)
    return commonInsert(input)
  }

  /**
   * Use Transaction
   * @return id value in string if succeed, else return null
   */
  fun insert(data: Map<String, Any>): String? {
    initialize()
    val input = InsertData(this, data)
    return commonInsert(input)
  }

  private fun commonInsert(input: InsertData): String? {
    input.init()

    val id = input.id
    // --- BEGIN Transaction ---
    val t = Connection.get().multi()
    t.hmset(genId(this, id), input.encodeData())
    input.stringIndices.forEach {
      t.sadd(genKey(this, it.key, it.value), id)
    }
    input.doubleIndices.forEach {
      t.zadd(genKey(this, it.key), mutableMapOf(id to it.value))
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