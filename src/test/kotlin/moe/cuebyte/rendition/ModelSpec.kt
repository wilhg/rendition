package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object ModelSpec : Spek({
  describe("model") {
    on("construct") {
      it("init with function") { Book }
      it("init with map") { Book2 }
    }

    on("properties") {
      it("is pk") {
        assertEquals(Book.pk.name, "id")
      }
      it("is string indices") {
        assertTrue { "author" in Book.stringIndices.map { it.name } }
        assertFalse { "words" in Book.stringIndices.map { it.name } }
      }
      it("is number indices") {
        assertTrue { "id" in Book2.doubleIndices.map { it.name } }
        assertFalse { "id" in Book.doubleIndices.map { it.name } }

        assertTrue { "words" in Book.doubleIndices.map { it.name } }
        assertFalse { "author" in Book.doubleIndices.map { it.name } }
      }
      it("is columns") {
        assertTrue { "id" in Book.columns.map { it.name } }
        assertTrue { "date" in Book.columns.map { it.name } }
      }
    }
  }
})


object Book : Model("book", {
  it["id"] = string().primaryKey().auto()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})

object Book2 : Model("book", mapOf(
    "id" to int().primaryKey(),
    "author" to string().index(),
    "publish" to string().index(),
    "words" to long().index(),
    "sales" to long().index(),
    "introduce" to string(),
    "date" to string()
))