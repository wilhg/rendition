package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun Result.update(): Boolean {
  val remStrIndex: MutableMap<Column, String> = HashMap()
  for ((name, col) in model.stringIndices) {
    remStrIndex[col] = this[name] as String
  }

  val id = this[model.pk.name]!! as String
  val t = Connection.get().multi()
  for ((col, value) in remStrIndex) {
    t.srem(genKey(model, col, value), id)
  }
  for ((name, col) in model.numberIndices) {
    t.zrem(genKey(model, col), id)
  }
  t.hdel(genId(model, id), *model.columns.keys.toTypedArray())

  return !t.exec().isEmpty()
}