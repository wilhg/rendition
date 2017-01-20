@file:JvmName("Model")
@file:JvmMultifileClass
package moe.cuebyte.rendition

import java.util.HashMap
import java.util.concurrent.ConcurrentHashMap

internal data class FoolFourReturn(
    val pk: Column,
    val strIndex: Map<String, Column>,
    val numIndex: Map<String, Column>,
    val columns: Map<String, Column>
)

abstract class Model {
  val name: String
  val pk: Column
  val stringIndices: Map<String, Column>
  val numberIndices: Map<String, Column>
  val columns: Map<String, Column>

  constructor(name: String, schema: Map<String, IncompleteColumn>) {
    this.name = name
    val (a, b, c, d) = initIndex(schema)
    pk = a; stringIndices = b; numberIndices = c; columns = d
  }

  constructor(name: String, body: (MutableMap<String, IncompleteColumn>)->Unit) {
    this.name = name
    val schema: MutableMap<String, IncompleteColumn> = HashMap()
    body(schema)
    val (a, b, c, d) = initIndex(schema)
    pk = a; stringIndices = b; numberIndices = c; columns = d
  }

  fun query(statement: Model.()->Calculator): ResultSet = statement(this).compute()

  private fun initIndex(schema: Map<String, IncompleteColumn>): FoolFourReturn {
    var tPk: Column? = null
    val tStringIndices: MutableMap<String, Column> = HashMap()
    val tDoubleIndices: MutableMap<String, Column> = HashMap()
    val tColumns: MutableMap<String, Column> = HashMap()

    for ((name, _col) in schema) {
      val col: Column = _col.complete(name)
      tColumns.put(col.name, col)
      when (col.meta) {
        Column.Meta.NONE -> {}
        Column.Meta.STRING_PK -> tPk = col;
        Column.Meta.NUMBER_PK -> {
          tPk = col; tDoubleIndices.put(col.name, col)
        }
        Column.Meta.STRING_INDEX -> tStringIndices.put(col.name, col)
        Column.Meta.NUMBER_INDEX -> tDoubleIndices.put(col.name, col)
      }
    }
    tPk ?:
        throw Exception("No primary key in schema.")

    return FoolFourReturn(tPk, tStringIndices, tDoubleIndices, tColumns)
  }
}