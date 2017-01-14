package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import redis.clients.jedis.Jedis
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object InsertMethodSpec : Spek({
  describe("insert method") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }
    it("should return id") {
      assertEquals(PostStr.insert {
        it["id"] = "a"
        it["name"] = "A"
        it["amount"] = 100
      }, genId(PostStr, "a"))
    }
  }

  describe("batch insert method") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
    }
    it("should return true") {
      assertTrue {
        PostStr.batchInsert(listOf(
            mapOf("id" to "123", "name" to "A", "amount" to 100),
            mapOf("id" to "124", "name" to "A", "amount" to 100)
        ))
      }
    }
  }
})