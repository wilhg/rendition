package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Model

internal class UpdateData(model: Model, input: Map<String, Any>) : InputData(model, input) {

  override fun idInit(input: Map<String, Any>) {}

  override fun indicesInit(input: Map<String, Any>) {
    for (col in model.stringIndices.values) {
      if (input[col.name] != null)
        tStrIndices.put(col, input[col.name] as String)
    }
    for (col in model.numberIndices.values) {
      if (input[col.name] != null)
        tNumIndices.put(col, (input[col.name] as Number).toDouble())
    }
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