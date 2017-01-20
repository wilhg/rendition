package moe.cuebyte.rendition.query.method

import moe.cuebyte.rendition.Calculator
import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.ResultSet

inline fun Model.query(statement: Model.()->Calculator): ResultSet = statement(this).compute()

infix fun Calculator.AND(resultSet: ResultSet): Calculator
    = this.addState(moe.cuebyte.rendition.Calculator.Op.AND, resultSet)

infix fun Calculator.OR(resultSet: ResultSet): Calculator
    = this.addState(moe.cuebyte.rendition.Calculator.Op.OR, resultSet)

infix fun Calculator.AND(calc: Calculator): Calculator
    = this.cat(moe.cuebyte.rendition.Calculator.Op.AND, calc)

infix fun Calculator.OR(calc: Calculator): Calculator
    = this.cat(moe.cuebyte.rendition.Calculator.Op.OR, calc)

infix fun ResultSet.AND(resultSet: ResultSet): Calculator
    = Calculator(this).addState(Calculator.Op.AND, resultSet)

infix fun ResultSet.OR(resultSet: ResultSet): Calculator
    = Calculator(this).addState(Calculator.Op.OR, resultSet)

infix fun ResultSet.AND(calc: Calculator): Calculator
    = Calculator(this).cat(moe.cuebyte.rendition.Calculator.Op.AND, calc)

infix fun ResultSet.OR(calc: Calculator): Calculator
    = Calculator(this).cat(moe.cuebyte.rendition.Calculator.Op.OR, calc)