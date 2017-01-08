package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.IdGenerator
import java.util.*

open class InputData(val model: Model, val input: Map<String, Any>){

  private var initialized = false
  internal lateinit var id: String
  internal val stringIndices: MutableMap<Column, String> = HashMap()
  internal val doubleIndices: MutableMap<Column, Double> = HashMap()
  internal val data: MutableMap<String, String> = HashMap()

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
    if (model.pk.checkType(input[pk.name]!!)) throw Exception("Id type was error.")

    id = input[pk.name].toString()
  }

  override fun indicesInit() {
    model.indexSet.forEach { idx ->
      input[idx.name] ?: throw Exception("Index-${idx.name} shall be defined.")
      if (!idx.checkType(input[idx.name]!!)) throw Exception("${idx.name} type error.")
      when(idx.type) {
        String::class.java -> stringIndices.put(idx, input[idx.name] as String)
        else -> doubleIndices.put(idx, input[idx.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach { col ->
      if (col.name !in input.keys) {
        data.put(col.name, col.default.toString())
      } else if (!col.checkType(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      } else {
        data.put(col.name, input[col.name].toString())
      }
    }
  }
}

class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit() {}

  override fun indicesInit() {
    model.indexSet.forEach { idx ->
      when(idx.type) {
        String::class.java -> stringIndices.put(idx, input[idx.name] as String)
        else -> doubleIndices.put(idx, input[idx.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach { col ->
      if (!col.checkType(input[col.name]!!)) {
        throw Exception("${col.name} type error.")
      }
      data.put(col.name, input[col.name].toString())
    }
  }
}