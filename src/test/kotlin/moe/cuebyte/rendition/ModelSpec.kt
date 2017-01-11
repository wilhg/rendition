package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object ModelSpec : Spek({
  describe("model property") {
    on("basic") {
      it("'s name should be ok") { assertEquals(Book.name, "book") }
      it("'s pk should be ok") {
      }
      it("'s stringIndices should be ok") { assertEquals(Book.name, "book") }
      it("'s numberIndices should be ok") { assertEquals(Book.name, "book") }
      it("'s columns should be ok") { assertEquals(Book.name, "book") }
    }
  }
})


object Book : Model("book", {
  it["id"] = int().primaryKey().auto()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})