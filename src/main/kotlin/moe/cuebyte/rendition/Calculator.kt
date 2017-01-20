package moe.cuebyte.rendition

import java.util.LinkedList

class Calculator(resultSet: ResultSet) : LinkedList<Calculator.State>() {

  enum class Op { HEAD, AND, OR }

  data class State(val op: Op, val resultSet: ResultSet)

  init {
    this.add(State(Op.HEAD, resultSet))
  }

  infix fun AND(resultSet: ResultSet): Calculator
      = this.addState(moe.cuebyte.rendition.Calculator.Op.AND, resultSet)

  infix fun OR(resultSet: ResultSet): Calculator
      = this.addState(moe.cuebyte.rendition.Calculator.Op.OR, resultSet)

  infix fun AND(calc: Calculator): Calculator
      = this.cat(moe.cuebyte.rendition.Calculator.Op.AND, calc)

  infix fun OR(calc: Calculator): Calculator
      = this.cat(moe.cuebyte.rendition.Calculator.Op.OR, calc)

  internal fun addState(op: Op, resultSet: ResultSet): Calculator {
    this.add(moe.cuebyte.rendition.Calculator.State(op, resultSet))
    return this
  }

  internal fun cat(op: Op, calc: Calculator): Calculator {
    if (calc.isEmpty()) return this
    this.add(State(op, calc.first().resultSet))
    this.addAll(calc.subList(1, calc.size))
    return this
  }

  internal fun compute(): ResultSet {
    if (this.isEmpty()) {
      throw Exception("The express in query block is none.")
    }
    var results = this[0].resultSet
    if (this.size == 1) {
      return results
    }
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