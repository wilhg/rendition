package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.mock.PostAuto
import moe.cuebyte.rendition.mock.PostStr
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFails

object InsertDataSpec : Spek({
  describe("InsertData") {
    on("init") {
      it("should ok with init") {
        InsertData(PostStr, mapOf("id" to "x", "name" to "A", "amount" to 100))
        InsertData(PostAuto, mapOf("name" to "A", "amount" to 100))
      }
      it("should fails with unmatched schema") {
        assertFails { InsertData(PostAuto, mapOf("name" to "A")) }
        assertFails { InsertData(PostAuto, mapOf("amount" to 100)) }
        assertFails { InsertData(PostAuto, mapOf("name" to "A", "echo" to "喵")) }
        assertFails { InsertData(PostStr, mapOf("name" to "A", "amount" to 100)) }
        assertFails { InsertData(PostStr, mapOf("id" to 0, "name" to "A", "amount" to 100)) }
      }
    }

    on("properties") {
      val body = mapOf("id" to "x", "name" to "A", "amount" to 100)
      val data = InsertData(PostStr, body)

      it("should be ok with id") {
        assertEquals(data.id, "x")
      }
      it("should be ok with numIndices") {
        assertEquals(data.numIndices.keys.first().name, "amount")
        assertEquals(data.numIndices.keys.first().meta, Column.Meta.NUMBER_INDEX)
      }
      it("should be ok with strIndices") {
        assertEquals(data.strIndices.keys.first().name, "name")
        assertEquals(data.strIndices.keys.first().meta, Column.Meta.STRING_INDEX)
      }
      it("should be ok with body") {
        assertEquals(data.body, body.mapValues { it.value.toString() })
      }
    }
  }
})