package moe.cuebyte.rendition.query.method

import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun Result.delete(): Boolean {
  val remStrIndex: MutableMap<String, String> = HashMap()
  for (name in model.stringIndices.keys) {
    remStrIndex[name] = this[name] as String
  }

  val id = this[model.pk.name] as String
  val t = Connection.get().multi()
  for ((name, value) in remStrIndex) {
    t.srem(genKey(model, name, value), id)
  }
  for (name in model.numberIndices.keys) {
    t.zrem(genKey(model, name), id)
  }
  t.del(genId(model, id))

  return t.exec().isNotEmpty()
}