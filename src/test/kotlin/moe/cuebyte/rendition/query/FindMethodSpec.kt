package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostAuto
import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.util.Connection
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object FindMethodSpec : Spek({
  describe("findMethodExt") {
    beforeGroup { Connection.set(Jedis("localhost")) }

    on("find") {
      val id = PostAuto.insert(mapOf("name" to "a", "amount" to 1))
      val res = PostStr.find(id!!)

      it("should ok") {
        assertEquals(res.keys.toSet(), setOf("id", "name", "amount"))
        assertEquals(res["name"], "a")
        assertEquals(res["amount"], 1)
      }
    }

    on("findBy") {
      PostStr.batchInsert(listOf(
          mapOf("id" to "1", "name" to "a", "amount" to 1),
          mapOf("id" to "2", "name" to "a", "amount" to 1),
          mapOf("id" to "3", "name" to "a", "amount" to 1)
      ))

      it("should ok") {
        PostStr.findBy("name", "a")
        PostStr.findBy("amount", 1)
      }
    }
  }
})