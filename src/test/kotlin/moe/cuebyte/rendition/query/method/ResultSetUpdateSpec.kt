package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.method.batchInsert
import moe.cuebyte.rendition.query.method.findBy
import moe.cuebyte.rendition.query.method.update
import moe.cuebyte.rendition.util.Connection
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import kotlin.test.assertEquals

object ResultSetUpdateSpec : Spek({
  describe("ResultSetUpdateSpec") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }


    on("update results") {
      PostStr.batchInsert(listOf(
          mapOf("id" to "1", "name" to "A", "amount" to 1)
          , mapOf("id" to "2", "name" to "A", "amount" to 2)
          , mapOf("id" to "3", "name" to "A", "amount" to 3)
      ))

      it("should be ok") {
        PostStr.findBy("name", "A").update(mapOf("name" to "B", "amount" to 0))
        assertEquals(PostStr.findBy("name", "B").size, 3)
        assertEquals(PostStr.findBy("amount", 0).size, 3)

      }

    }
  }
})