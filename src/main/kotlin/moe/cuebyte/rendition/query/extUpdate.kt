package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.ResultSet
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun Result.update(data: Map<String, Any>): String? {
  val tUpdateData = HashMap(this)

  val remStrIndex: MutableMap<String, String> = HashMap()

  for ((key, value) in data) {
    if (model.stringIndices.containsKey(key)) {
      remStrIndex[key] = this[key] as String
    }
    tUpdateData[key] = value
  }

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