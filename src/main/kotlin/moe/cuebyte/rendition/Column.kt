package moe.cuebyte.rendition

class Column(val model: Model, val type: Class<*>, val default: Any) {
  internal var name: String = ""
  internal var isPk = false; private set
  internal var isIndex = false; private set
  internal var automated = false; internal set

  fun primaryKey(): Column {
    if (isPk || isIndex) throw Exception("a column should only be set as primaryKey or with an index")
    isPk = true
    return this
  }

  fun auto(): Column {
    if (!isPk) throw Exception("Only Id could be generate automatically.")
    automated = true
    return this
  }

  fun index(): Column {
    if (isPk || isIndex) throw Exception("a column should only be set as an primaryKey or with an index")
    isIndex = true
    return this
  }

  fun genKey(value: String = ""): String {
    return when {
      this.isPk -> "${model.name}:$value"
      this.isIndex -> "${model.name}:$name:$value"
      else -> throw Exception("error")
    }
  }

  fun checkType(value: Any) = value.javaClass == type
}