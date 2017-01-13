package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model
import java.util.*

internal typealias MutableMultiIndex<T> = MutableMap<Column, MutableMap<T, MutableList<String>>>
internal typealias MultiIndex<T> = Map<Column, Map<T, List<String>>>

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
    model.stringIndices.forEach { tmStrMultiIndex[it] = HashMap() }
    model.numberIndices.forEach { tmNumMultiIndex[it] = HashMap() }

    for (input in batchInput) {
      val okInput: Map<String, String> = model.columns
          .map { it.name to (input[it.name] ?: it.default).toString() }
          .toMap()

      tPks.add(okInput[model.pk.name]!!.toString())
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
    if (model.columns.map { it.name }.toSet() != input.keys.toSet()) {
      throw Exception("Data do not match the schema.")
    }
    if (!model.columns.all {
      input[it.name] == null || it.checkType(input[it.name]!!)
    }) {
      throw Exception("Data do not match the schema.")
    }
  }
}