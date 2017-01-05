package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.IdGenerator
import java.util.*

open class Input : HashMap<String, Any> {

  val model: Model
  private var initialized = false
  internal lateinit var id: String
  internal val stringIndices: MutableMap<Column, String> = HashMap()
  internal val doubleIndices: MutableMap<Column, Double> = HashMap()
  internal val data: MutableMap<Column, String> = HashMap()

  constructor(model: Model) : super() {
    this.model = model
  }

  constructor(model: Model, data: Map<String, Any>) : super(data) {
    this.model = model
  }

  open protected fun idInit() {}
  open protected fun indicesInit() {}
  open protected fun dataInit() {}

  fun init() {
    if (initialized) return
    val tmpSet = keys.toHashSet()
    tmpSet.removeAll(model.columnSet.map { it.name })
    if (!tmpSet.isEmpty()) throw Exception("Data do not match the schema.")

    idInit()
    indicesInit()
    dataInit()
    initialized = true
  }

  fun encodeData(): Map<String, String> {
    val map = HashMap<String, String>()
    data.forEach { map.put(it.key.name, it.value) }
    return map
  }
}

class InsertData : Input {

  constructor(model: Model) : super(model)
  constructor(model: Model, data: Map<String, Any>) : super(model, data)

  override fun idInit() {
    val pk = model.pk
    if (pk.automated) {
      id = IdGenerator.next()
      return
    }
    if (this[pk.name] == null || this[pk.name] == "") throw Exception("Id has not defined.")
    if (model.pk.checkType(this[pk.name]!!)) throw Exception("Id type was error.")

    id = this[pk.name].toString()
  }

  override fun indicesInit() {
    model.indexSet.forEach { index ->
      this[index.name] ?: throw Exception("Index-${index.name} shall be defined.")
      if (!index.checkType(this[index.name]!!)) throw Exception("${index.name} type error.")
      when (this[index.name]) {
        is String -> stringIndices.put(index, this[index.name] as String)
        is Number -> doubleIndices.put(index, this[index.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach {
      if (it.name !in keys) {
        data.put(it, it.default.toString())
      } else if (!it.checkType(this[it.name]!!)) {
        throw Exception("${it.name} type error.")
      } else {
        data.put(it, this[it.name].toString())
      }
    }
  }
}

class UpdateData : Input {

  constructor(model: Model) : super(model)
  constructor(model: Model, data: Map<String, Any>) : super(model, data)

  override fun idInit() {}
  override fun indicesInit() {
    model.indexSet.forEach {
      when (this[it.name]) {
        is String -> stringIndices.put(it, this[it.name] as String)
        is Number -> doubleIndices.put(it, this[it.name] as Double)
      }
    }
  }

  override fun dataInit() {
    model.columnSet.forEach {
      if (!it.checkType(this[it.name]!!)) {
        throw Exception("${it.name} type error.")
      } else {
        data.put(it, this[it.name].toString())
      }
    }
  }
}