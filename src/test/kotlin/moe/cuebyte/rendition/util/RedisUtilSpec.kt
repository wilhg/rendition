package moe.cuebyte.rendition.util

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
object Book : Model("book", {
  it["id"] = int().primaryKey().auto()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})
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