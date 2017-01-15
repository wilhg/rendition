package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.ResultSet
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun Result.update(data: Map<String, Any>): String? {
  val remStrIndex: MutableMap<String, String> = HashMap()

  data.keys
      .filter { model.stringIndices.containsKey(it) }
      .forEach { remStrIndex[it] = this[it] as String }

  val updateData = UpdateData(model, data)
  val id = updateData.id

  val t = Connection.get().multi()
  for ((k, v) in remStrIndex) { // Delete index in set only
    val col = model.stringIndices[k]!!
    t.srem(genKey(model, col, v), id)
  }

  t.hmset(genId(model, id), updateData.body)
  for ((col, value) in updateData.strIndices) {
    t.sadd(genKey(model, col, value), id)
  }
  for ((col, value) in updateData.numIndices) {
    t.zadd(genKey(model, col), value, id)
  }
  return if (t.exec().isEmpty()) {
    null
  } else {
    genId(model, id)
  }
}