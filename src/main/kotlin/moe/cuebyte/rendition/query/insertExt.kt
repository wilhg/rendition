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
fun <T : Model> T.insert(data: Map<String, Any>): String? {
  val input = InsertData(this, data)
  return commonInsert(input)
}

/**
 * Use Transaction
 * @return id value in string if succeed, else return null
 */
fun <T : Model> T.insert(body: (MutableMap<String, Any>)->Unit): String? {
  val map = HashMap<String, Any>()
  body(map)
  return insert(map)
}

private fun <T : Model> T.commonInsert(data: InsertData): String? {
  val id = data.id
  // --- BEGIN Transaction ---
  val t = Connection.get().multi()
  t.hmset(genId(this, id), data.body)
  data.strIndices.forEach {
    t.sadd(genKey(this, it.key, it.value), id)
  }
  data.numIndices.forEach {
    t.zadd(genKey(this, it.key), mapOf(id to it.value))
  }
  return if (t.exec().isEmpty()) null else id
  // --- END Transaction ---
}