package moe.cuebyte.rendition.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object IdGeneratorSpec : Spek({
  describe("Id Generator") {
    on("next") {
      it("default length should be 16") {
        assert(IdGenerator.next().length == 16)
      }
      it("should be available defined the length") {
        val len = (Math.random() * 100).toInt()
        assert(IdGenerator.next(len).length == len)
      }
    }
  }
})