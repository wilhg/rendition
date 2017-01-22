package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model
import java.util.HashMap
import java.util.LinkedList

private typealias MutableMultiIndex<T> = MutableMap<Column, MutableMap<T, MutableList<String>>>
private typealias MultiIndex<T> = Map<Column, Map<T, List<String>>>

/**
 * The batch input body have to include Id
 */
internal class BatchInsertData(val model: Model, batchInput: List<Map<String, Any>>) {

  internal val bodies: List<Map<String, String>>
  internal val strMultiIndex: MultiIndex<String>
  internal val numMultiIndex: MultiIndex<Double>

  init {
    // `t` = template
    // `m` = multi
    val tPks: MutableList<String> = LinkedList()
    val tBodies: MutableList<Map<String, String>> = LinkedList()
    val tmStrMultiIndex: MutableMultiIndex<String> = HashMap()
    val tmNumMultiIndex: MutableMultiIndex<Double> = HashMap()

    checkInput(batchInput[0])

    for (col in model.stringIndices.values) {
      tmStrMultiIndex[col] = HashMap()
    }
    for (col in model.numberIndices.values) {
      tmStrMultiIndex[col] = HashMap()
    }

    for (input in batchInput) {
      val okInput: Map<String, String> = model.columns
          .map { (name, col) -> name to (input[name] ?: col.default).toString() }
          .toMap()
      tPks.add(okInput[model.pk.name] as String)
      tBodies.add(okInput)

      for ((col, idxMap) in tmStrMultiIndex) {
        val idxValue = okInput[col.name]!!
        if (idxMap[idxValue] == null) {
          idxMap[idxValue] = LinkedList()
        }
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
      for ((col, idxMap) in tmNumMultiIndex) {
        val idxValue = okInput[col.name]!!.toDouble()
        if (idxMap[idxValue] == null) {
          idxMap[idxValue] = LinkedList()
        }
        idxMap[idxValue]!!.add(okInput[model.pk.name]!!)
      }
    }

    bodies = tBodies; strMultiIndex = tmStrMultiIndex; numMultiIndex = tmNumMultiIndex
  }

  private fun checkInput(input: Map<String, Any>) {
    if (model.columns.keys.toSet() != input.keys.toSet()) {
      throw Exception("Data do not match the schema.")
    }
    if (!model.columns.values.all {
      input[it.name] == null || it.checkType(input[it.name]!!)
    }) {
      throw Exception("Data do not match the schema.")
    }
  }
}