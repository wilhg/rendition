package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey
import java.util.HashMap

/**
 * Use Transaction
 * @return id value in string if succeed, else return null
 */
fun Model.insert(data: Map<String, Any>): String? {
  val input = InsertData(this, data)
  return commonInsert(input)
}

/**
 * Use Transaction
 * @return id value in string if succeed, else return null
 */
fun Model.insert(body: (MutableMap<String, Any>)->Unit): String? {
  val map = HashMap<String, Any>()
  body(map)
  return insert(map)
}

private fun Model.commonInsert(data: InsertData): String? {
  val id = data.id
  // --- BEGIN Transaction ---
  val t = Connection.get().multi()
  t.hmset(genId(this, id), data.body)
  for ((col, value) in data.strIndices) {
    t.sadd(genKey(this, col, value), id)
  }
  for ((col, value) in data.numIndices) {
    t.zadd(genKey(this, col), value, id)
  }
  return if (t.exec().isEmpty()) {
    null
  } else {
    genId(this,id)
  }
  // --- END Transaction ---
}