package moe.cuebyte.redition

import com.sun.org.apache.xpath.internal.operations.Bool
import moe.cuebyte.redition.Util.Connection
import java.util.*


class Lockable<T>(val data: T) {
  var locked: Boolean = false; private set

  /**
   * @return succeeded or failed
   */
  fun runThenLock(cb: (T)->Unit): Boolean {
    if (locked) return false

    cb(data)
    locked = true
    return true
  }

  fun lock() {
    locked = true
  }

  fun unlock() {
    locked = false
  }
}

abstract class Model(val name: String) {

  /**
   * This property can only be modified before the Model instance was used.
   */
  val schema: MutableMap<String, Column> = HashMap()

  private val initialized: Boolean = false
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
        pk = col
        if (pk.type == Boolean::class.java) throw Exception("Primary key type error.")
        if (pk.type != String::class.java) {
          indexSet.add(pk)
          pk.automated = false
        }
      }
      if (col.isIndex) indexSet.add(col)
    }
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
    t.hmset(pk.genKey(id), input.encodeData())
    input.stringIndices.forEach {
      t.sadd(it.key.genKey(it.value), id)
    }
    input.doubleIndices.forEach {
      t.zadd(it.key.genKey(), mutableMapOf(id to it.value))
    }
    return if (t.exec().isEmpty()) null else id
    // --- END Transaction ---
  }

  protected fun bool(default: Boolean = false) = Column(this, Boolean::class.java, default)
  protected fun string(default: String = "") = Column(this, String::class.java, default)
  protected fun int(default: Int = 0) = Column(this, Int::class.java, default)
  protected fun long(default: Long = 0) = Column(this, Long::class.java, default)
  protected fun float(default: Float = 0f) = Column(this, Float::class.java, default)
  protected fun double(default: Double = 0.0) = Column(this, Double::class.java, default)
}