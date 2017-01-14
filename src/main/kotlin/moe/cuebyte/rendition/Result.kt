package moe.cuebyte.rendition

import redis.clients.jedis.Response

class Result(private val model: Model, private val resp: Response<Map<String, String>>)
  : Map<String, Any?> {

  override val entries get() = lazyValue.entries
  override val values get() = lazyValue.values
  override val keys get() = lazyValue.keys
  override val size get() = lazyValue.size

  override fun containsValue(value: Any?) = lazyValue.containsValue(value)
  override fun containsKey(key: String) = lazyValue.containsKey(key)
  override fun get(key: String) = lazyValue[key]
  override fun isEmpty() = lazyValue.isEmpty()

  private val lazyValue by lazy { init() }

  private fun init(): Map<String, Any?> {
    val data = resp.get()
    return model.columns.map { col ->
      val datum = data[col.name]
      when (col.type) {
        String::class.java  -> col.name to datum
        Int::class.java     -> col.name to datum?.toInt()
        Double::class.java  -> col.name to datum?.toDouble()
        Boolean::class.java -> col.name to datum?.toBoolean()
        Float::class.java   -> col.name to datum?.toFloat()
        Long::class.java    -> col.name to datum?.toLong()
        else                -> col.name to null
      }
    }.toMap()
  }
}

class ResultSet(private val model: Model) : HashSet<Result>() {
  val and get() = model
  val or get() = model
}