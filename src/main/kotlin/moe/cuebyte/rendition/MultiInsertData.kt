package moe.cuebyte.rendition

import java.util.*

/**
 * The batch input body have to include Id
 */
class MultiInsertData(val model: Model, batchInput: List<Map<String, Any>>) {

  internal val pks: List<String>
  internal val batchBody: List<Map<String, String>>
  internal val batchStrIndices: Map<Column, Map<String, List<String>>>
  internal val batchNumIndices: Map<Column, Map<Double, List<String>>>

  init {
    // `t` = template
    // `b` = batch
    val tPks: MutableList<String> = LinkedList()
    val tbBody: MutableList<Map<String, String>> = LinkedList()
    val tbStrIndices: MutableMap<Column, MutableMap<String, MutableList<String>>> = HashMap()
    val tbNumIndices: MutableMap<Column, MutableMap<Double, MutableList<String>>> = HashMap()

    checkInput(batchInput[-1])
    model.stringIndices.forEach { tbStrIndices[it] = HashMap() }
    model.doubleIndices.forEach { tbNumIndices[it] = HashMap() }

    batchInput.forEach { input ->
      val okInput: Map<String, String> = model.columns
          .map { it.name to (input[it.name] ?: it.default).toString() }
          .toMap()

      tPks.add(okInput[model.pk.name]!!.toString())
      tbBody.add(okInput)
      tbStrIndices.forEach {
        val idxMap = it.value
        val idxValue = okInput[it.key.name]!!
        if (idxMap[idxValue] == null) idxMap[idxValue] = LinkedList()
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
      tbNumIndices.forEach {
        val idxMap = it.value
        val idxValue = okInput[it.key.name]!!.toDouble()
        if (idxMap[idxValue] == null) idxMap[idxValue] = LinkedList()
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
    }

    pks = tPks; batchBody = tbBody; batchStrIndices = tbStrIndices; batchNumIndices = tbNumIndices
  }

  private fun checkInput(input: Map<String, Any>) {
    if (!checkColsName(input)) throw Exception("Data do not match the schema.")
    if (input[model.pk.name] == null) {
      throw Exception("In batchInsert, Id must be set. No matter weather the automated was true")
    }
    if (!model.columns.all {
      input[it.name] == null || it.checkType(input[it.name]!!)
    }) {
      throw Exception("Data do not match the schema.")
    }
  }

  private fun checkColsName(input: Map<String, Any>): Boolean {
    val inputKeys = input.keys.toHashSet()
    inputKeys.removeAll(model.columns.map { it.name })
    return inputKeys.isEmpty()
  }
}