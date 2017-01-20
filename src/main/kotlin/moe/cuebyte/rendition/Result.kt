package moe.cuebyte.rendition

import redis.clients.jedis.Response

class Result(val model: Model, private val resp: Response<Map<String, String>>)
  : Map<String, Any> {

  override val entries get() = lazyValue.entries
  override val values get() = lazyValue.values
  override val keys get() = lazyValue.keys
  override val size get() = lazyValue.size

  override fun containsValue(value: Any) = lazyValue.containsValue(value)
  override fun containsKey(key: String) = lazyValue.containsKey(key)
  override fun get(key: String) = lazyValue[key]
  override fun isEmpty() = lazyValue.isEmpty()

  private val lazyValue by lazy { init() }

  private fun init(): Map<String, Any> {
    val data = resp.get()
    return model.columns.map { (name, col) ->
      val datum = data[name]!!
      when (col.type) {
        String::class.java -> name to datum
        Int::class.java -> name to datum.toInt()
        Double::class.java -> name to datum.toDouble()
        Boolean::class.java -> name to datum.toBoolean()
        Float::class.java -> name to datum.toFloat()
        Long::class.java -> name to datum.toLong()
        else -> throw Exception("Internal error")
      }
    }.toMap()
  }
}

class ResultSet(val model: Model) : HashSet<Result>() {

}