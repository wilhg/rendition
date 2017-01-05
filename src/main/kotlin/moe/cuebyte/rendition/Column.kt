package moe.cuebyte.rendition

class Column(val type: Class<*>, val default: Any) {

  internal lateinit var name: String // Initialized at Model::initialize
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

  fun validate(value: Any) = value.javaClass == type
}