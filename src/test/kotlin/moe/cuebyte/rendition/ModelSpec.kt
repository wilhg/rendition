package moe.cuebyte.rendition

import moe.cuebyte.rendition.mock.BookInt
import moe.cuebyte.rendition.mock.BookNone
import moe.cuebyte.rendition.mock.BookStr
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object ModelSpec : Spek({
  describe("model") {
    on("construct") {
      it("init with function") { BookStr }
      it("init with map") { BookInt }
      it("init with map") { assertFails { BookNone } }
    }

    on("properties") {
      it("is pk") {
        assertEquals(BookStr.pk.name, "id")
      }
      it("is string indices") {
        assertTrue { "author" in BookStr.stringIndices.map { it.name } }
        assertFalse { "words" in BookStr.stringIndices.map { it.name } }
      }
      it("is number indices") {
        assertTrue { "id" in BookInt.numberIndices.map { it.name } }
        assertFalse { "id" in BookStr.numberIndices.map { it.name } }

        assertTrue { "words" in BookStr.numberIndices.map { it.name } }
        assertFalse { "author" in BookStr.numberIndices.map { it.name } }
      }
      it("is columns") {
        assertTrue { "id" in BookStr.columns.map { it.name } }
        assertTrue { "date" in BookStr.columns.map { it.name } }
      }
    }
  }
})