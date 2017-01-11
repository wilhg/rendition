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
  it["id"] = string().primaryKey().auto()
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
      it("gen hash key") {
        assertEquals(genKey(Book, Book.stringIndices[0], "Shakes"), "book:author:Shakes")
      }
      it("gen sorted set key") {
        assertEquals(genKey(Book, Book.doubleIndices[0]), "book:words")
      }
    }
  }
})