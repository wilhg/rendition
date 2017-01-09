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
  mInsert.batchStrIndices.forEach {
    val col = it.key
    val idxMap = it.value
    idxMap.forEach {
      val idxName = it.key
      val ids = it.value
      t.sadd(genKey(this, col, idxName), *ids.toTypedArray())
    }
  }
  mInsert.batchNumIndices.forEach {
    val col = it.key
    val idxMap = it.value
    idxMap.forEach {
      val idxName = it.key
      val ids = it.value
      ids.forEach { id -> t.zadd(genKey(this, col), idxName, id) }
    }
  }
  return !t.exec().isEmpty()
}