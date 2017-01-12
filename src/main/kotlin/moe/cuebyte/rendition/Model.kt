package moe.cuebyte.rendition

import java.util.ArrayList
import java.util.HashMap

internal data class FoolFourReturn(
    val pk: Column,
    val strIndex: List<Column>,
    val numIndex: List<Column>,
    val columns: List<Column>)

abstract class Model {
  val name: String
  val pk: Column
  val stringIndices: List<Column>
  val numberIndices: List<Column>
  val columns: List<Column>

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


  private fun initIndex(schema: Map<String, IncompleteColumn>): FoolFourReturn {
    var tPk: Column? = null
    val tStringIndices: MutableList<Column> = ArrayList()
    val tDoubleIndices: MutableList<Column> = ArrayList()
    val tColumns: MutableList<Column> = ArrayList()

    for ((name, _col) in schema) {
      val col: Column = _col.complete(name)
      tColumns.add(col)
      when (col.meta) {
        Column.Meta.NONE -> {}
        Column.Meta.STRING_PK -> tPk = col;
        Column.Meta.NUMBER_PK -> {
          tPk = col; tDoubleIndices.add(col)
        }
        Column.Meta.STRING_INDEX -> tStringIndices.add(col)
        Column.Meta.NUMBER_INDEX -> tDoubleIndices.add(col)
      }
    }
    if (tPk == null) {
      throw Exception("No primary key in schema.")
    }
    return FoolFourReturn(tPk, tStringIndices, tDoubleIndices, tColumns)
  }
}