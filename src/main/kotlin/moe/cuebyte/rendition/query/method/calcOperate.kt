package moe.cuebyte.rendition.query.method

import moe.cuebyte.rendition.Calculator
import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.ResultSet

inline fun Model.query(statement: Model.()->Calculator): ResultSet {
  // TODO 在这里设置用户态ID？
  statement(this)
}

infix fun Calculator.AND(results: ResultSet): Calculator {

}

infix fun Calculator.OR(results: ResultSet): Calculator {

}

infix fun Calculator.AND(calc: Calculator): Calculator {

}

infix fun Calculator.OR(calc: Calculator): Calculator {

}

infix fun ResultSet.AND(results: ResultSet): Calculator {

}

infix fun ResultSet.OR(results: ResultSet): Calculator {

}

infix fun ResultSet.AND(calc: Calculator): Calculator {

}

infix fun ResultSet.OR(calc: Calculator): Calculator {

}