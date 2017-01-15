package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Result
import moe.cuebyte.rendition.ResultSet

fun Result.update(data: Map<String, Any>) {
  if (!this.model.columns.keys.containsAll(data.keys)) {
    throw Exception("schema unmatched")
  }
  if (this.model.pk.name in data.keys) {
    throw Exception("Couldn't modify id")
  }
  for ((key, value) in data) {
    this.model
  }
}

fun ResultSet.update() {

}