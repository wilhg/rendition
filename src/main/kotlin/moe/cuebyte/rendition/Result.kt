package moe.cuebyte.rendition

import redis.clients.jedis.Response

class Result(private val model: Model, private val resp: Response<Map<String, String>>) {

  private val lazyValue by lazy { initValue() }

  fun get() = lazyValue

  private fun initValue(): Map<String, Any?> {
    val data = resp.get()
    return model.columns.map { col ->
      val datum = data[col.name]
      if (datum == null) {
        col.name to null
      } else {
        when (col.type) {
          String::class.java -> col.name to datum
          Int::class.java -> col.name to datum.toInt()
          Double::class.java -> col.name to datum.toDouble()
          Boolean::class.java -> col.name to datum.toBoolean()
          Float::class.java -> col.name to datum.toFloat()
          Long::class.java -> col.name to datum.toLong()
          else -> col.name to null
        }
      }
    }.toMap()
  }
}