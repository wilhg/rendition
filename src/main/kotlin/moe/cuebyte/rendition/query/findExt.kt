package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey
import java.util.LinkedList

fun Model.find(id: Number) = find(id.toString())

fun Model.find(id: String): Result {
  val p = Connection.get().pipelined()
  val resp = p.hgetAll(id)
  p.sync()
  return Result(this, resp)
}

fun Model.findBy(name: String, value: String): List<Result> {
  val col = this.stringIndices[name] ?:
      throw Exception("Index doesn't exist")
  val ids = Connection.get().smembers(genKey(this, col, value))
  return getResults(ids)
}

fun Model.findBy(name: String, value: Number): List<Result> {
  val col = this.numberIndices[name] ?:
      throw Exception("Index doesn't exist")
  val ids = Connection.get().zrangeByScore(genKey(this, col), value.toDouble(), value.toDouble())
  return getResults(ids)
}

private fun Model.getResults(ids: Set<String>): List<Result> {
  val p = Connection.get().pipelined()
  val results = ids.mapTo(LinkedList()) { Result(this, p.hgetAll(genId(this, it))) }
  p.sync()
  return results
}