package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.util.IdGenerator
import java.util.HashMap

@Suppress("LeakingThis")
internal abstract class InputData(val model: Model, input: Map<String, Any>) {

  val id: String
  val body: Map<String, String>
  val strIndices: Map<Column, String>
  val numIndices: Map<Column, Double> // Jedis score is Double

  protected var tId: String = ""
  protected val tBody: MutableMap<String, String> = HashMap()
  protected val tStrIndices: MutableMap<Column, String> = HashMap()
  protected val tNumIndices: MutableMap<Column, Double> = HashMap()

  open protected fun idInit(input: Map<String, Any>) {}
  open protected fun indicesInit(input: Map<String, Any>) {}
  open protected fun dataInit(input: Map<String, Any>) {}

  init {
    if (!checkColsName(input)) {
      throw Exception("Data do not match the schema.")
    }
    idInit(input)
    indicesInit(input)
    dataInit(input)
    id = tId; strIndices = tStrIndices; numIndices = tNumIndices; body = tBody
  }

  private fun checkColsName(input: Map<String, Any>): Boolean {
    val tmpSet = input.keys.toHashSet()
    tmpSet.removeAll(model.columns.keys)
    return tmpSet.isEmpty()
  }
}