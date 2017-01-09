package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun <T : Model> T.batchInsert(batch: List<Map<String, Any>>): Boolean {
  val bInsert = BatchInsertData(this, batch)
  val t = Connection.get().multi()
  
  for (body in bInsert.batchBody) {
    t.hmset(genId(this, body[this.pk.name]!!), body)
  }
  for ((col, idsMap) in bInsert.batchStrIndices) {
    for ((idxName, ids) in idsMap) {
      t.sadd(genKey(this, col, idxName), *ids.toTypedArray())
    }
  }
  for ((col, idsMap) in bInsert.batchNumIndices) {
    for ((idxName, ids) in idsMap) {
      ids.forEach { t.zadd(genKey(this, col), idxName, it) }
    }
  }
  return !t.exec().isEmpty()
}