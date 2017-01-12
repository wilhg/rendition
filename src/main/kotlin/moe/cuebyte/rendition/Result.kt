package moe.cuebyte.rendition

import redis.clients.jedis.Response

class Result(private val model: Model, private val resp: Response<Map<String, String>>)
  : Map<String, Any?> {

  override val entries: Set<Map.Entry<String, Any?>>
    get() = lazyValue.entries
  override val keys: Set<String>
    get() = lazyValue.keys
  override val size: Int
    get() = lazyValue.size
  override val values: Collection<Any?>
    get() = lazyValue.values

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