package moe.cuebyte.rendition

import java.util.*


class MultiInsertData(val model: Model, batchInput: List<Map<String, Any>>) {

  private var initialized = false
  private val batchData: MutableList<Map<String, String>> = LinkedList()
  private val batchSIndices: MutableMap<Column, MutableList<String>> = HashMap()
  private val batchDIndices: MutableMap<Column, MutableList<Double>> = HashMap()

  init {
    if (!checkColsName(batchInput[0]) || !checkColsName(batchInput[-1])) {
      throw Exception("Data do not match the schema.")
    }
    batchInput.forEach { input ->
      val okInput = model.columnSet
          .map { it.name to (input[it.name] ?: it.default).toString() }
          .toMap()

      batchData.add(okInput)
      model.indexSet.forEach { idx ->
        when(idx.type) {
          String::class.java -> batchSIndices[]
        }
      }
    }
  }

  private fun checkColsName(input: Map<String,Any>): Boolean {
    val tmpSet = input.keys.toHashSet()
    tmpSet.removeAll(model.columnSet.map { it.name })
    return tmpSet.isEmpty()
  }
}