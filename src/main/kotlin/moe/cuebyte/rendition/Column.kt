package moe.cuebyte.rendition

class Column
internal constructor(val name: String, type: Class<*>, default: Any, val info: Info, val automated: Boolean)
  : IncompleteColumn(type, default) {
  enum class Info {
    NONE,
    STRING_PK, DOUBLE_PK,
    STRING_INDEX, DOUBLE_INDEX
  }

  fun checkType(value: Any): Boolean {
    return when (value) {
      is String -> type == String::class.java
      is Double -> type == Double::class.java
      is Int -> type == Int::class.java
      is Long -> type == Long::class.java
      is Float -> type == Float::class.java
      else -> false
    }
  }
}

open class IncompleteColumn(val type: Class<*>, val default: Any) {

  private var automated = false
  private var info: Column.Info = Column.Info.NONE

  fun primaryKey(): IncompleteColumn {
    if (info != Column.Info.NONE) {
      throw Exception("a column should only be set as primaryKey or with an index")
    }
    if (type == Boolean::class.java) {
      throw Exception("Primary key could not be bool.")
    }
    info = if (type == String::class.java) {
      Column.Info.STRING_PK
    } else {
      Column.Info.DOUBLE_PK
    }
    return this
  }

  fun auto(): IncompleteColumn {
    if (info !in arrayOf(Column.Info.STRING_PK, Column.Info.DOUBLE_PK)) {
      throw Exception("Only Id could be generate automatically.")
    }
    automated = true
    return this
  }

  fun index(): IncompleteColumn {
    if (info != Column.Info.NONE) {
      throw Exception("a column should only be set as an primaryKey or with an index")
    }
    if (type == Boolean::class.java) {
      throw Exception("Index could not be bool.")
    }
    info = if (type == String::class.java) {
      Column.Info.STRING_INDEX
    } else {
      Column.Info.DOUBLE_INDEX
    }
    return this
  }

  fun complete(name: String): Column = Column(name, this.type, this.default, info, automated)
}