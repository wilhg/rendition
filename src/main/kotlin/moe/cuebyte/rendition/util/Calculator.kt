package moe.cuebyte.rendition.util

import moe.cuebyte.rendition.Model

internal class Calculator(val type: Type) {
  enum class Type { AND, OR }

  private var next: Calculator? = null

  fun and(vararg op: Model.()->Unit): Calculator {
    val cal = Calculator(Type.AND)
    this.next = cal
  }

  fun or(vararg op: Model.()->Unit): Calculator {
    val cal = Calculator(Type.OR)
    this.next = cal
  }

  fun tail(): Calculator {
    var cal: Calculator = this
    while (cal.next != null) {
      cal = cal.next!!
    }
    return cal
  }
}