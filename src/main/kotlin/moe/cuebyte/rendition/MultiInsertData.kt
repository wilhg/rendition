package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.IdGenerator
import java.util.*


class MultiInsertData(val model: Model, batchInput: List<MutableMap<String, Any>>) {

  internal val pks: MutableList<String> = LinkedList()
  internal val batchData: MutableList<Map<String, String>> = LinkedList()
  internal val batchSIndices: MutableMap<Column, MutableList<String>> = HashMap()
  internal val batchDIndices: MutableMap<Column, MutableList<Double>> = HashMap()

  init {
    checkInput(batchInput[0])

    batchInput.forEach { input ->
      val pkName = model.pk.name
      input[pkName] = handleId(input[pkName])
      val okInput: Map<String, String> = model.columns
          .map { it.name to (input[it.name] ?: it.default).toString() }
          .toMap()

      pks.add(okInput[pkName]!!.toString())
      batchData.add(okInput)
      model.stringIndices.forEach {
        if (batchSIndices[it] == null) batchSIndices[it] = LinkedList()
        batchSIndices[it]!!.add(okInput[it.name]!!)
      }
      model.doubleIndices.forEach {
        if (batchDIndices[it] == null) batchDIndices[it] = LinkedList()
        batchDIndices[it]!!.add(okInput[it.name]!!.toDouble())
      }
    }
  }

  private fun handleId(value: Any?): Any {
    val pk = model.pk

    if (pk.automated) return IdGenerator.next()
    if (value == null || value == "") throw Exception("Id has not defined.")
    if (model.pk.checkType(value)) throw Exception("Id type was error.")

    return value
  }

  private fun checkInput(input: Map<String, Any>) {
    if (!checkColsName(input)) throw Exception("Data do not match the schema.")

    if (!model.columns.all {
      input[it.name] == null || it.checkType(input[it.name]!!)
    }) {
      throw Exception("Data do not match the schema.")
    }
  }

  private fun checkColsName(input: Map<String, Any>): Boolean {
    val tmpSet = input.keys.toHashSet()
    tmpSet.removeAll(model.columns.map { it.name })
    return tmpSet.isEmpty()
  }

}