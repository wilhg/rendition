package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import moe.cuebyte.rendition.util.genKey

fun Model.find(id: Number) = find(id.toString())

fun Model.find(id: String): Result {
  val p = Connection.get().pipelined()
  val resp = p.hgetAll(id)
  p.sync()
  return Result(this, resp)
}

fun Model.findBy(index: String): List<Result> {
  val p = Connection.get().pipelined()
  p.sync()
}

fun Model.findBy(index: Number): List<Result> {
  val p = Connection.get().pipelined()
  p.sync()
}