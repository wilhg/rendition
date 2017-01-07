package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.IdGenerator
import java.util.*

open class InputData(val model: Model, val input: Map<String, Any>){

  private var initialized = false
  internal lateinit var id: String
  internal val stringIndices: MutableMap<Column, String> = HashMap()
  internal val doubleIndices: MutableMap<Column, Double> = HashMap()
  internal val data: MutableMap<Column, String> = HashMap()

  open protected fun idInit() {}
  open protected fun indicesInit() {}
  open protected fun dataInit() {}

  fun init() {
    if (initialized) return
    if (!checkColsName()) throw Exception("Data do not match the schema.")

    idInit()
    indicesInit()
    dataInit()
    initialized = true
  }

  internal fun encodeData(): Map<String, String> {
    if (!initialized) throw Exception("Internal error, InputData hasn't been initialized.")
    return data.mapKeys { it.key.name }
  }

  private fun checkColsName(): Boolean {
    val tmpSet = input.keys.toHashSet()
    tmpSet.removeAll(model.columnSet.map { it.name })
    return tmpSet.isEmpty()
  }
}

class InsertData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit() {
    val pk = model.pk
    if (pk.automated) {
      id = IdGenerator.next()
      return
    }
    if (input[pk.name] == null || input[pk.name] == "") throw Exception("Id has not defined.")
    if (model.pk.validate(input[pk.name]!!)) throw Exception("Id type was error.")

    id = input[pk.name].toString()
  }

  override fun indicesInit() {
    model.indexSet.forEach { idx ->
      input[idx.name] ?: throw Exception("Index-${idx.name} shall be defined.")
      if (!idx.validate(input[idx.name]!!)) throw Exception("${idx.name} type error.")
      when (input[idx.name]) {
        is String -> stringIndices.put(idx, input[idx.name] as String)
        is Number -> doubleIndices.put(idx, input[idx.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach { col ->
      if (col.name !in input.keys) {
        data.put(col, col.default.toString())
      } else if (!col.validate(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      } else {
        data.put(col, input[col.name].toString())
      }
    }
  }
}

class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit() {}
  override fun indicesInit() {
    model.indexSet.forEach { index ->
      when (input[index.name]) {
        is String -> stringIndices.put(index, input[index.name] as String)
        is Number -> doubleIndices.put(index, input[index.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach { col ->
      if (!col.validate(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      }
      data.put(col, input[col.name].toString())
    }
  }
}