package moe.cuebyte.rendition.query.data

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.data.BatchInsertData
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFails

object BatchInsertDataSpec : Spek({
  describe("batch insert data") {
    on("init") {
      it("valid") {
        BatchInsertData(PostStr, listOf(
            mapOf("id" to "123", "name" to "A", "amount" to 100),
            mapOf("id" to "124", "name" to "A", "amount" to 100)))
      }
      it("invalid") {
        assertFails { BatchInsertData(PostStr, listOf(mapOf("name" to "A", "amount" to 1))) }
        assertFails { BatchInsertData(PostStr, listOf(mapOf("id" to "1", "name" to "A", "amount" to 1, "?" to "?"))) }
        assertFails { BatchInsertData(PostStr, listOf(mapOf("id" to "2", "name" to "A", "amount" to "1"))) }
        assertFails { BatchInsertData(PostStr, listOf(mapOf("id" to "3", "name" to 1, "amount" to 1))) }
        assertFails { BatchInsertData(PostStr, listOf(mapOf("id" to "4", "name" to "A"))) }
        assertFails { BatchInsertData(PostStr, listOf(mapOf("id" to "5", "amount" to 1))) }
      }
    }
  }
})