package moe.cuebyte.rendition

import java.util.LinkedList

class Calculator(resultSet: ResultSet) : LinkedList<Calculator.State>() {

  enum class Op { HEAD, AND, OR }

  data class State(val op: Op, val resultSet: ResultSet)

  init {
    this.add(State(Op.HEAD, resultSet))
  }

  fun addState(op: Op, resultSet: ResultSet): Calculator {
    this.add(moe.cuebyte.rendition.Calculator.State(op, resultSet))
    return this
  }

  fun cat(op: Op, calc: Calculator): Calculator {
    if (calc.isEmpty()) return this
    this.add(State(op, calc.first().resultSet))
    this.addAll(calc.subList(1, calc.size))
    return this
  }

  fun compute(): ResultSet {
    var results = this[0].resultSet

    for ((op, set) in this.subList(1, this.size)) {
      when (op) {
        Op.AND -> results = results.intersect(set)
        Op.OR -> results = results.union(set)
        else -> throw Exception("The express in query block is illegal.")
      }
    }
    return results
  }
}