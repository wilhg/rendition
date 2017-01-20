package moe.cuebyte.rendition

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.findBy
import moe.cuebyte.rendition.query.range
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on

object ResultSetSpec : Spek({
  describe("") {
    on("") {
      PostStr.statement {
        findBy("name", "a") AND (range("amount", 0, 1) OR range("amount", 3, 4))
      }
    }
  }
})