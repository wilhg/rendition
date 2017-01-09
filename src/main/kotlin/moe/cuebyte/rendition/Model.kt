package moe.cuebyte.rendition

import moe.cuebyte.rendition.Util.Connection
import moe.cuebyte.rendition.Util.genId
import moe.cuebyte.rendition.Util.genKey
import java.util.*

internal data class FoolFourReturn(
    val pk: Column,
    val sIndex: List<Column>,
    val dIndex: List<Column>,
    val columns: List<Column>
)

abstract class Model {
  val name: String
  val pk: Column
  val stringIndices: List<Column>
  val doubleIndices: List<Column>
  val columns: List<Column>

  constructor(name: String, schema: MutableMap<String, IncompleteColumn>) {
    this.name = name
    val (a, b, c, d) = initIndex(schema)
    pk = a; stringIndices = b; doubleIndices = c; columns = d
  }

  constructor(name: String, body: (MutableMap<String, Column>)->Unit) {
    this.name = name
    val schema: MutableMap<String, Column> = HashMap()
    body(schema)
    val (a, b, c, d) = initIndex(schema)
    pk = a; stringIndices = b; doubleIndices = c; columns = d
  }

  /**
   * Use Transaction
   * @return id value in string if succeed, else return null
   */
  fun insert(data: Map<String, Any>): String? {
    val input = InsertData(this, data)
    return commonInsert(input)
  }

  /**
   * Use Transaction
   * @return id value in string if succeed, else return null
   */
  fun insert(body: (MutableMap<String, Any>)->Unit): String? {
    val map = HashMap<String, Any>()
    body(map)
    return insert(map)
  }

  fun batchInsert(batch: List<Map<String, Any>>): Boolean {
    val mInsert = MultiInsertData(this, batch)
    val t = Connection.get().multi()
    mInsert.batchBody.forEach { data ->
      t.hmset(genId(this, data[this.pk.name]!!), data)
    }
    mInsert.batchStrIndices.forEach {
      val col = it.key
      val idxMap = it.value
      idxMap.forEach {
        val idxName = it.key
        val ids = it.value
        t.sadd(genKey(this, col, idxName), *ids.toTypedArray())
      }
    }
    mInsert.batchNumIndices.forEach {
      val col = it.key
      val idxMap = it.value
      idxMap.forEach {
        val idxName = it.key
        val ids = it.value
        ids.forEach { id -> t.zadd(genKey(this, col), idxName, id) }
      }
    }
    return !t.exec().isEmpty()
  }

  private fun commonInsert(data: InsertData): String? {
    val id = data.id
    // --- BEGIN Transaction ---
    val t = Connection.get().multi()
    t.hmset(genId(this, id), data.body)
    data.strIndices.forEach {
      t.sadd(genKey(this, it.key, it.value), id)
    }
    data.numIndices.forEach {
      t.zadd(genKey(this, it.key), mapOf(id to it.value))
    }
    return if (t.exec().isEmpty()) null else id
    // --- END Transaction ---
  }

  private fun initIndex(schema: Map<String, IncompleteColumn>): FoolFourReturn {
    var tPk: Column? = null
    val tStringIndices: MutableList<Column> = ArrayList()
    val tDoubleIndices: MutableList<Column> = ArrayList()
    val tColumns: MutableList<Column> = ArrayList()

    schema.forEach {
      val col: Column = it.value.complete(it.key)
      tColumns.add(col)
      when (col.info) {
        IncompleteColumn.Info.NONE -> {
        }
        IncompleteColumn.Info.STRING_PK -> {
          tPk = col; tPk!!.automated = false
        }
        IncompleteColumn.Info.DOUBLE_PK -> {
          tPk = col; tDoubleIndices.add(col)
        }
        IncompleteColumn.Info.STRING_INDEX -> tStringIndices.add(col)
        IncompleteColumn.Info.DOUBLE_INDEX -> tDoubleIndices.add(col)
      }
    }
    tPk ?: throw Exception("No primary key in schema.")
    return FoolFourReturn(tPk!!, tStringIndices, tDoubleIndices, tColumns)
  }
}