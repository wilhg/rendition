@file:JvmName("Result")
@file:JvmMultifileClass

package moe.cuebyte.rendition

import redis.clients.jedis.Response
import java.util.LinkedList
import java.util.TreeSet

class Result(val model: Model, private val resp: Response<Map<String, String>>)
  : Map<String, Any> {

  override val entries get() = lazyMap.entries
  override val values get() = lazyMap.values
  override val keys get() = lazyMap.keys
  override val size get() = lazyMap.size

  override fun containsValue(value: Any) = lazyMap.containsValue(value)
  override fun containsKey(key: String) = lazyMap.containsKey(key)
  override fun get(key: String) = lazyMap[key]
  override fun isEmpty() = lazyMap.isEmpty()

  override fun toString() = lazyMap.toString()

  internal val id: String get() = lazyMap[pkName].toString()

  private val pkName: String = model.pk.name
  private val lazyMap: Map<String, Any> by lazy { init() }

  /**
   * For better performance, avoid using lambda
   */
  private fun init(): Map<String, Any> {
    val data = resp.get()
    val outputMap = HashMap<String, Any>()

    for ((name, col) in model.columns) {
      val datum = data[name]!!
      val value: Any = when (col.type) {
        String::class.java -> datum
        Int::class.java -> datum.toInt()
        Double::class.java -> datum.toDouble()
        Boolean::class.java -> datum.toBoolean()
        Float::class.java -> datum.toFloat()
        Long::class.java -> datum.toLong()
        else -> throw Exception("Internal error")
      }
      outputMap.put(name, value)
    }
    return outputMap
  }
}

class ResultSet : HashSet<Result> {
  val model: Model

  constructor(model: Model) : super() {
    this.model = model
  }

  private constructor(model: Model, results: List<Result>) : super(results) {
    this.model = model
  }

  override fun toString(): String {
    var str = "[\n"
    for (res in this) {
      str += "${res.toString()},\n"
    }
    str += "]\n"
    return str
  }

  infix fun AND(resultSet: ResultSet): Calculator
      = Calculator(this).addState(Calculator.Op.AND, resultSet)

  infix fun OR(resultSet: ResultSet): Calculator
      = Calculator(this).addState(Calculator.Op.OR, resultSet)

  infix fun AND(calc: Calculator): Calculator
      = Calculator(this).cat(moe.cuebyte.rendition.Calculator.Op.AND, calc)

  infix fun OR(calc: Calculator): Calculator
      = Calculator(this).cat(moe.cuebyte.rendition.Calculator.Op.OR, calc)

  /**
   * For better performance, avoid using lambda
   */
  internal fun intersect(resultSet: ResultSet): ResultSet {
    val (bigger, smaller) = getPair(this, resultSet)
    val biggerIds = LinkedList<String>()
    val smallerIds = LinkedList<String>()
    for (result in bigger) biggerIds.add(result.id)
    for (result in smaller) smallerIds.add(result.id)

    val idSet = TreeSet(biggerIds)
    idSet.retainAll(smallerIds)

    val resultList = LinkedList<Result>()
    for (result in this) {
      if (idSet.contains(result.id))
        resultList.add(result)
    }
    return ResultSet(model, resultList)
  }

  /**
   * For better performance, avoid using lambda and sugars
   */
  internal fun union(resultSet: ResultSet): ResultSet {
    val (bigger, smaller) = getPair(this, resultSet)
    val biggerIds = LinkedList<String>()
    for (result in bigger) biggerIds.add(result.id)

    val baseIdTree = TreeSet(biggerIds)
    for (result in smaller) {
      if (!baseIdTree.contains(result.id))
        bigger.add(result)
    }
    return bigger
  }

  /**
   * @return Pair(Bigger, Smaller)
   */
  private fun getPair(a: ResultSet, b: ResultSet): Pair<ResultSet, ResultSet> {
    return if (a.size > b.size) {
      Pair<ResultSet, ResultSet>(a, b)
    } else {
      Pair<ResultSet, ResultSet>(b, a)
    }
  }
}