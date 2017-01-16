package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.mock.PostAuto
import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.data.UpdateData
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

object UpdateDataSpec : Spek({
  describe("UpdateDataSpec") {
    on("init") {
      it("should ok with init") {
        UpdateData(PostStr, mapOf("id" to "x", "name" to "A", "amount" to 100))

      }
      it("should fails with unmatched schema") {
        assertFails { UpdateData(PostAuto, mapOf("id" to "x", "name" to 1, "amount" to 100)) }
        assertFails { UpdateData(PostAuto, mapOf("id" to "x", "name" to "A", "amount" to "")) }
        assertFails { UpdateData(PostAuto, mapOf("id" to "x", "name" to "A", "echo" to "喵")) }
        assertFails { UpdateData(PostStr, mapOf("id" to 0, "name" to "A", "amount" to 100)) }
      }
    }

    on("properties") {
      val body1 = mapOf("id" to "x", "amount" to 100)
      val body2 = mapOf("id" to "x", "name" to "A")
      val data1 = UpdateData(PostStr, body1)
      val data2 = UpdateData(PostStr, body2)

      it("should be ok with numIndices") {
        assertEquals(data1.numIndices.keys.first().name, "amount")
        assertEquals(data1.numIndices.keys.first().meta, Column.Meta.NUMBER_INDEX)

        assertTrue { data1.strIndices.isEmpty() }
      }
      it("should be ok with strIndices") {
        assertEquals(data2.strIndices.keys.first().name, "name")
        assertEquals(data2.strIndices.keys.first().meta, Column.Meta.STRING_INDEX)

        assertTrue { data2.numIndices.isEmpty() }
      }
      it("should be ok with body") {
        assertEquals(data1.body, body1.mapValues { it.value.toString() })
        assertEquals(data2.body, body2.mapValues { it.value.toString() })
      }
    }
  }
})