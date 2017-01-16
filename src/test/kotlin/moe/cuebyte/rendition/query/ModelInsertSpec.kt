package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostAuto
import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genId
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import redis.clients.jedis.Jedis
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ModelInsertSpec : Spek({
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
      }, "a")
    }

    it("should ok with auto id") {
      val id = PostAuto.insert {
        it["name"] = "A"
        it["amount"] = 100
      }
      assertEquals(PostAuto.find(id!!)["id"], id)
    }
  }
})