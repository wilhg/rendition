package moe.cuebyte.rendition

open class IncompletedColumn(val type: Class<*>, val default: Any) {
  enum class Info {
    NONE,
    STRING_PK,
    DOUBLE_PK,
    STRING_INDEX,
    DOUBLE_INDEX
  }
  private var info: Info = Info.NONE

  internal var automated = false

  fun primaryKey(): IncompletedColumn {
    if (info != Info.NONE) throw Exception("a column should only be set as primaryKey or with an index")
    if (type == String::class.java) info = Info.STRING_PK
    else info = Info.DOUBLE_PK
    return this
  }

  fun auto(): IncompletedColumn {
    if (info == Info.STRING_PK || info == Info.DOUBLE_PK) {
      throw Exception("Only Id could be generate automatically.")
    }
    automated = true
    return this
  }

  fun index(): IncompletedColumn {
    if (info != Info.NONE) throw Exception("a column should only be set as an primaryKey or with an index")
    if (type == String::class.java) info = Info.STRING_INDEX
    else info = Info.DOUBLE_INDEX
    return this
  }

  fun complete(name: String): Column = Column(name, this.type, this.default, info)

  fun checkType(value: Any) = value.javaClass == type
}

class Column
internal constructor(val name: String, type: Class<*>, default: Any, val info: Info)
  : IncompletedColumn(type, default)

fun bool(default: Boolean = false) = IncompletedColumn(Boolean::class.java, default)
fun string(default: String = "") = IncompletedColumn(String::class.java, default)
fun int(default: Int = 0) = IncompletedColumn(Int::class.java, default)
fun long(default: Long = 0) = IncompletedColumn(Long::class.java, default)
fun float(default: Float = 0f) = IncompletedColumn(Float::class.java, default)
fun double(default: Double = 0.0) = IncompletedColumn(Double::class.java, default)