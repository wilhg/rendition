package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.ResultSet
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun ResultSet.delete(): Boolean {
  val remStrIndex: MutableMap<String, String> = HashMap()
  for (name in model.stringIndices.keys) {
    remStrIndex[name] = this.first()[name] as String
  }

  val ids = this.map { it[model.pk.name] as String }.toTypedArray()
  val t = Connection.get().multi()

  for ((name, value) in remStrIndex) {
    t.srem(genKey(model, name, value), *ids)
  }
  for ((colName, _) in model.numberIndices) {
    t.zrem(genKey(model, colName), *ids)
  }
  ids.forEach { t.del(genId(model, it)) }

  return !t.exec().isEmpty()
}