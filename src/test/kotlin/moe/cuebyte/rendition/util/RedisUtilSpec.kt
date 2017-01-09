package moe.cuebyte.rendition.util

import moe.cuebyte.rendition.mock.Book
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object RedisUtilSpec : Spek({
  describe("RedisUtilSpec") {
    on("gen key or id") {
      it("gen id") {
        assert(genId(Book, "abc") == "${Book.name}:abc")
      }
      it("gen key") {
        assert(genKey(Book, Book.columns[-1]) == "${Book.name}:${Book.columns[-1]}")
      }
      it("gen key2") {
        assert(genKey(Book, Book.columns[-1], "abc") == "${Book.name}:${Book.columns[-1]}:abc")
      }
    }
  }
})