package moe.cuebyte.rendition.query

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
    tmpSet.removeAll(model.columns.map { it.name })
    return tmpSet.isEmpty()
  }
}

internal class InsertData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {
    val pk = model.pk
    if (pk.automated) {
      tId = IdGenerator.next()
      return
    }
    if (input[pk.name] == null || input[pk.name] == "") {
      throw Exception("Id has not defined.")
    }
    if (!model.pk.checkType(input[pk.name]!!)) {
      throw Exception("Id type was error.")
    }

    tId = input[pk.name].toString()
  }

  override fun indicesInit(input: Map<String, Any>) {
    model.stringIndices.forEach { idx ->
      if (input[idx.name] == null) {
        throw Exception("Index-${idx.name} shall be defined.")
      }
      if (input[idx.name]!! !is String) {
        throw Exception("${idx.name} should be String.")
      }
      tStrIndices.put(idx, input[idx.name] as String)
    }
    model.numberIndices.forEach { idx ->
      if (input[idx.name] == null) {
        throw Exception("Index-${idx.name} shall be defined.")
      }
      if (!idx.checkType(input[idx.name]!!)) {
        throw Exception("${idx.name} type error.")
      }
      tNumIndices.put(idx, (input[idx.name] as Number).toDouble())
    }
  }

  override fun dataInit(input: Map<String, Any>) {
    model.columns.forEach { col ->
      if (col.name !in input.keys) {
        tBody.put(col.name, col.default.toString())
      } else if (!col.checkType(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      } else {
        tBody.put(col.name, input[col.name].toString())
      }
    }
  }
}

internal class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {
    val pkValue = input[model.pk.name]
    if (pkValue == null || pkValue == "") {
      throw Exception("Id has not defined.")
    }
    tId = pkValue.toString()
  }

  override fun indicesInit(input: Map<String, Any>) {
    model.stringIndices.forEach { idx ->
      if (input[idx.name] == null) {
        return
      }
      if (input[idx.name] !is String) {
        throw Exception("${idx.name} should be String.")
      }
      tStrIndices.put(idx, input[idx.name] as String)
    }
    model.numberIndices.forEach { idx ->
      if (input[idx.name] == null) {
        return
      }
      if (!idx.checkType(input[idx.name]!!)) {
        throw Exception("${idx.name} type error.")
      }
      tNumIndices.put(idx, (input[idx.name] as Number).toDouble())
    }
  }

  override fun dataInit(input: Map<String, Any>) {
    model.columns.forEach { col ->
      if (!col.checkType(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      }
      tBody.put(col.name, input[col.name].toString())
    }
  }
}