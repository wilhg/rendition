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
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }

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

      it("should ok with str index") {
        PostStr.findBy("name", "a")
            .forEach { assertEquals(it["name"], "a") }
      }

      it("should ok with num index") {
        PostStr.findBy("amount", 1)
            .forEach { assertEquals(it["amount"], 1) }
      }
    }
  }
})