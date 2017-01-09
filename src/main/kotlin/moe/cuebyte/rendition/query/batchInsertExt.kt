package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun <T : Model> T.batchInsert(batch: List<Map<String, Any>>): Boolean {
  val mInsert = BatchInsertData(this, batch)
  val t = Connection.get().multi()
  mInsert.batchBody.forEach { data ->
    t.hmset(genId(this, data[this.pk.name]!!), data)
  }
  for ((col, idMap) in mInsert.batchStrIndices) {
    for ((idxName, ids) in idMap) {
      t.sadd(genKey(this, col, idxName), *ids.toTypedArray())
    }
  }
  for ((col, idMap) in mInsert.batchNumIndices) {
    for ((idxName, ids) in idMap) {
      ids.forEach { t.zadd(genKey(this, col), idxName, it) }
    }
  }
  return !t.exec().isEmpty()
}