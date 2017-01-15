package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.ResultSet
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun ResultSet.update(data: Map<String, Any>): Boolean {
  val remStrIndex: MutableMap<String, String> = HashMap()
  for (name in model.stringIndices.keys) {
    remStrIndex[name] = this.first()[name] as String
  }

  val ids = this.map { it[model.pk.name]!! as String }.toTypedArray()
  val updateData = UpdateData(model, data)
  val t = Connection.get().multi()

  for ((name, value) in remStrIndex) {
    t.srem(genKey(model, name, value), *ids)
  }

  ids.forEach { t.hmset(genId(model, it), updateData.body) }
  for ((col, value) in updateData.strIndices) {
    t.sadd(genKey(model, col, value), *ids)
  }
  for ((col, value) in updateData.numIndices) {
    t.zadd(genKey(model, col), ids.map { (it to value) }.toMap())
  }

  return !t.exec().isEmpty()
}