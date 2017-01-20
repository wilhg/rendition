package moe.cuebyte.rendition

import redis.clients.jedis.Response
import java.util.TreeSet

class Result(val model: Model, private val resp: Response<Map<String, String>>)
  : Map<String, Any> {

  override val entries get() = lazyValue.entries
  override val values get() = lazyValue.values
  override val keys get() = lazyValue.keys
  override val size get() = lazyValue.size

  override fun containsValue(value: Any) = lazyValue.containsValue(value)
  override fun containsKey(key: String) = lazyValue.containsKey(key)
  override fun get(key: String) = lazyValue[key]
  override fun isEmpty() = lazyValue.isEmpty()

  internal val id: String get() = lazyValue[pkName].toString()

  private val pkName: String = model.pk.name
  private val lazyValue: Map<String, Any> by lazy { init() }

  private fun init(): Map<String, Any> {
    val data = resp.get()
    return model.columns.map { (name, col) ->
      val datum = data[name]!!
      when (col.type) {
        String::class.java -> name to datum
        Int::class.java -> name to datum.toInt()
        Double::class.java -> name to datum.toDouble()
        Boolean::class.java -> name to datum.toBoolean()
        Float::class.java -> name to datum.toFloat()
        Long::class.java -> name to datum.toLong()
        else -> throw Exception("Internal error")
      }
    }.toMap()
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

  internal fun intersect(resultSet: ResultSet): ResultSet {
    val (bigger, smaller) = getPair(this, resultSet)
    val set = TreeSet(bigger.map(Result::id))
    set.retainAll(smaller.map(Result::id))
    return ResultSet(model, this.filter { it.id in set })
  }

  internal fun union(resultSet: ResultSet): ResultSet {
    val (bigger, smaller) = getPair(this, resultSet)
    val baseIds = bigger.map(Result::id).toSortedSet()
    smaller
        .filter { it.id !in baseIds }
        .forEach { bigger.add(it) }
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