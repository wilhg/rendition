package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Model

internal class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {
//    val pkValue = input[model.pk.name]
//    if (pkValue == null || pkValue == "") {
//      throw Exception("Id has not defined.")
//    }
//    tId = pkValue.toString()
  }

  override fun indicesInit(input: Map<String, Any>) {
    model.stringIndices.values
        .filter { input[it.name] != null }
        .forEach { tStrIndices.put(it, input[it.name] as String) }

    model.numberIndices.values
        .filter { input[it.name] != null }
        .forEach { tNumIndices.put(it, (input[it.name] as Number).toDouble()) }
  }

  override fun dataInit(input: Map<String, Any>) {
    for ((name, col) in model.columns) {
      if (!col.checkType(input[name] ?: continue)) {
        throw Exception("$name type error.")
      }
      tBody.put(name, input[name].toString())
    }
  }
}