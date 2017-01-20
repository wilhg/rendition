package moe.cuebyte.rendition.query.method

import moe.cuebyte.rendition.ResultSet
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey
import java.util.LinkedList

fun ResultSet.delete(): Boolean {
  val remStrIndex: MutableMap<String, List<String>> = HashMap()
  for (name in model.stringIndices.keys) {
    remStrIndex.put(name, ArrayList(this.map { it[name] as String }))
  }

  val ids = this.map { it[model.pk.name] as String }.toTypedArray()
  val t = Connection.get().multi()

  for ((name, list) in remStrIndex) {
    for (value in list) {
      t.srem(genKey(model, name, value), *ids)
    }
  }
  for (name in model.numberIndices.keys) {
    t.zrem(genKey(model, name), *ids)
  }
  ids.forEach { t.del(genId(model, it)) }

  return !t.exec().isEmpty()
}