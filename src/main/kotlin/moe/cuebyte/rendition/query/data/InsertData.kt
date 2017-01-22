package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.util.IdGenerator

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
    for (idx in model.stringIndices.values) {
      input[idx.name] ?:
          throw Exception("Index-${idx.name} shall be defined.")

      if (input[idx.name]!! !is String) {
        throw Exception("${idx.name} should be String.")
      }
      tStrIndices.put(idx, input[idx.name] as String)
    }
    for (idx in model.numberIndices.values) {
      input[idx.name] ?:
          throw Exception("Index-${idx.name} shall be defined.")

      if (!idx.checkType(input[idx.name]!!)) {
        throw Exception("${idx.name} type error.")
      }
      tNumIndices.put(idx, (input[idx.name] as Number).toDouble())
    }
  }

  override fun dataInit(input: Map<String, Any>) {
    for ((name, col) in model.columns) {
      if (name !in input.keys) {
        tBody.put(name, col.default.toString())
      } else if (!col.checkType(input[name]!!)) {
        throw Exception("$name type error.")
      } else {
        tBody.put(name, input[name].toString())
      }
    }
    if (model.pk.automated) {
      tBody.put(model.pk.name, tId)
    }
  }
}