package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.IdGenerator
import java.util.*

@Suppress("LeakingThis")
open class InputData(val model: Model, input: Map<String, Any>) {

  internal val id: String
  internal val body: Map<String, String>
  internal val stringIndices: Map<Column, String>
  internal val doubleIndices: Map<Column, Double>

  protected var tId: String = ""
  protected val tsi: MutableMap<Column, String> = HashMap()
  protected val tdi: MutableMap<Column, Double> = HashMap()
  protected val tBody: MutableMap<String, String> = HashMap()

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
    id = tId; stringIndices = tsi; doubleIndices = tdi; body = tBody
  }

  private fun checkColsName(input: Map<String, Any>): Boolean {
    val tmpSet = input.keys.toHashSet()
    tmpSet.removeAll(model.stringIndices.map { it.name })
    tmpSet.removeAll(model.doubleIndices.map { it.name })
    return tmpSet.isEmpty()
  }
}

class InsertData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {
    val pk = model.pk
    if (pk.automated) {
      tId = IdGenerator.next()
      return
    }
    if (input[pk.name] == null || input[pk.name] == "") throw Exception("Id has not defined.")
    if (model.pk.checkType(input[pk.name]!!)) throw Exception("Id type was error.")

    tId = input[pk.name].toString()
  }

  override fun indicesInit(input: Map<String, Any>) {
    model.stringIndices.forEach { idx ->
      input[idx.name] ?: throw Exception("Index-${idx.name} shall be defined.")
      if (input[idx.name]!! !is String) throw Exception("${idx.name} should be String.")
      tsi.put(idx, input[idx.name] as String)
    }
    model.doubleIndices.forEach { idx ->
      input[idx.name] ?: throw Exception("Index-${idx.name} shall be defined.")
      if (!idx.checkType(input[idx.name]!!)) throw Exception("${idx.name} type error.")
      tdi.put(idx, (input[idx.name] as Number).toDouble())
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

class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {
    val pkValue = input[model.pk.name]
    if (pkValue == null || pkValue == "") throw Exception("Id has not defined.")
    tId = pkValue.toString()
  }

  override fun indicesInit(input: Map<String, Any>) {
    model.stringIndices.forEach { idx ->
      input[idx.name] ?: return
      if (input[idx.name] !is String) throw Exception("${idx.name} should be String.")
      tsi.put(idx, input[idx.name] as String)
    }
    model.doubleIndices.forEach { idx ->
      input[idx.name] ?: return
      if (!idx.checkType(input[idx.name]!!)) throw Exception("${idx.name} type error.")
      tdi.put(idx, (input[idx.name] as Number).toDouble())
    }
  }

  override fun dataInit(input: Map<String, Any>) {
    model.columns.forEach { col ->
      if (!col.checkType(input[col.name]!!)) throw Exception("${col.name} type error.")
      tBody.put(col.name, input[col.name].toString())
    }
  }
}