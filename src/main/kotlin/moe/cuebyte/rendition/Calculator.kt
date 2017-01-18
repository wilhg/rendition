package moe.cuebyte.rendition

class Calculator(
    private val op: Op,
    private val data: List<ResultSet>) {

  enum class Op { AND, OR }

  private var next: Calculator? = null

  fun tail(): Calculator {
    var cal: Calculator = this
    while (cal.next != null) {
      cal = cal.next!!
    }
    return cal
  }
}