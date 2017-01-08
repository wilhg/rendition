package moe.cuebyte.rendition

class Column
internal constructor(val name: String, type: Class<*>, default: Any, val info: Info)
  : IncompleteColumn(type, default)

fun bool(default: Boolean = false) = IncompleteColumn(Boolean::class.java, default)
fun string(default: String = "") = IncompleteColumn(String::class.java, default)
fun int(default: Int = 0) = IncompleteColumn(Int::class.java, default)
fun long(default: Long = 0) = IncompleteColumn(Long::class.java, default)
fun float(default: Float = 0f) = IncompleteColumn(Float::class.java, default)
fun double(default: Double = 0.0) = IncompleteColumn(Double::class.java, default)

open class IncompleteColumn(val type: Class<*>, val default: Any) {
  enum class Info {
    NONE,
    STRING_PK, DOUBLE_PK,
    STRING_INDEX, DOUBLE_INDEX
  }

  internal var automated = false
  private var info: Info = Info.NONE

  fun primaryKey(): IncompleteColumn {
    if (info != Info.NONE) {
      throw Exception("a column should only be set as primaryKey or with an index")
    }
    if (type == Boolean::class.java) {
      throw Exception("Primary key could not be bool.")
    }
    info = if (type == String::class.java) {
      Info.STRING_PK
    } else {
      Info.DOUBLE_PK
    }
    return this
  }

  fun auto(): IncompleteColumn {
    if (info == Info.STRING_PK || info == Info.DOUBLE_PK) {
      throw Exception("Only Id could be generate automatically.")
    }
    if (type == Boolean::class.java) {
      throw Exception("Index could not be bool.")
    }
    automated = true
    return this
  }

  fun index(): IncompleteColumn {
    if (info != Info.NONE) {
      throw Exception("a column should only be set as an primaryKey or with an index")
    }
    info = if (type == String::class.java) {
      Info.STRING_INDEX
    } else {
      Info.DOUBLE_INDEX
    }
    return this
  }

  fun complete(name: String): Column = Column(name, this.type, this.default, info)

  fun checkType(value: Any) = value.javaClass == type
}