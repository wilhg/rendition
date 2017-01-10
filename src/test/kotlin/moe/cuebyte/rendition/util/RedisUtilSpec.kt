package moe.cuebyte.rendition.util

import moe.cuebyte.rendition.mock.Book
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object RedisUtilSpec : Spek({
  describe("RedisUtilSpec") {
    on("gen key or id") {
      it("gen id") {
        assertEquals(genId(Book, "abc"), "${Book.name}:abc")
      }
      it("gen key") {
        assertEquals(genKey(Book, Book.columns[0]), "${Book.name}:${Book.columns[0]}")
      }
      it("gen key2") {
        assertEquals(genKey(Book, Book.columns[0], "abc"), "${Book.name}:${Book.columns[0]}:abc")
      }
    }
  }
})