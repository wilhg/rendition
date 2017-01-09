package moe.cuebyte.rendition

import java.util.*

/**
 * The batch input data have to include Id
 */
class MultiInsertData(val model: Model, batchInput: List<Map<String, Any>>) {

  internal val pks: List<String>
  internal val batchData: List<Map<String, String>>
  internal val batchSIndices: Map<Column, Map<String, List<String>>>
  internal val batchDIndices: Map<Column, Map<Double, List<String>>>

  init {
    val tPks: MutableList<String> = LinkedList()
    val tBatchData: MutableList<Map<String, String>> = LinkedList()
    val tBatchSIndices: MutableMap<Column, MutableMap<String, MutableList<String>>> = HashMap()
    val tBatchDIndices: MutableMap<Column, MutableMap<Double, MutableList<String>>> = HashMap()

    checkInput(batchInput[0])
    model.stringIndices.forEach { tBatchSIndices[it] = HashMap() }
    model.doubleIndices.forEach { tBatchDIndices[it] = HashMap() }

    batchInput.forEach { input ->
      val okInput: Map<String, String> = model.columns
          .map { it.name to (input[it.name] ?: it.default).toString() }
          .toMap()

      tPks.add(okInput[model.pk.name]!!.toString())
      tBatchData.add(okInput)
      tBatchSIndices.forEach {
        val idxMap = it.value
        val idxValue = okInput[it.key.name]!!
        if (idxMap[idxValue] == null) idxMap[idxValue] = LinkedList()
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
      tBatchDIndices.forEach {
        val idxMap = it.value
        val idxValue = okInput[it.key.name]!!.toDouble()
        if (idxMap[idxValue] == null) idxMap[idxValue] = LinkedList()
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
    }

    pks = tPks; batchData = tBatchData; batchSIndices = tBatchSIndices; batchDIndices = tBatchDIndices
  }

  private fun checkInput(input: Map<String, Any>) {
    if (!checkColsName(input)) throw Exception("Data do not match the schema.")
    if (input[model.pk.name] == null) throw Exception("In batchInsert, Id must be set. " +
        "No matter weather the automated was true")
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