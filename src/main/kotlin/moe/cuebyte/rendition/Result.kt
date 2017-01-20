package moe.cuebyte.rendition

import redis.clients.jedis.Response

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

  private val lazyValue by lazy { init() }

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

class ResultSet(val model: Model) : HashSet<Result>() {

  fun and(vararg methods: Model.()->ResultSet): ResultSet {
    val cal = Calculator(Calculator.Op.AND, methods.map { it(model) })
    this.next = cal
    return cal
  }

  fun or(vararg methods: Model.()->ResultSet): ResultSet {
    val cal = Calculator(Calculator.Op.OR, methods.map { it(model) })
    this.next = cal
    return cal
  }

  infix fun AND(results: ResultSet): Calculator {

  }

  infix fun OR(results: ResultSet): Calculator {

  }

  infix fun AND(cal: Calculator): Calculator {

  }

  infix fun OR(cal: Calculator): Calculator {

  }
}