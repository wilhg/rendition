package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.method.batchInsert
import moe.cuebyte.rendition.query.method.delete
import moe.cuebyte.rendition.query.method.findBy
import moe.cuebyte.rendition.util.Connection
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import kotlin.test.assertTrue

object ResultSetDeleteSpec : Spek({
  describe("ResultSetDeleteSpec") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }

    on("delete results") {
      PostStr.batchInsert(listOf(
          mapOf("id" to "1", "name" to "A", "amount" to 1)
          , mapOf("id" to "2", "name" to "A", "amount" to 2)
          , mapOf("id" to "6", "name" to "A", "amount" to 4)
          , mapOf("id" to "3", "name" to "B", "amount" to 3)
          , mapOf("id" to "4", "name" to "C", "amount" to 3)
          , mapOf("id" to "5", "name" to "D", "amount" to 3)
      ))

      it("should be ok with string index.") {
        PostStr.findBy("name", "A").delete()
        assertTrue { PostStr.findBy("name", "A").isEmpty() }
        assertTrue { PostStr.findBy("amount", 1).isEmpty() }
        assertTrue { PostStr.findBy("amount", 2).isEmpty() }
        assertTrue { PostStr.findBy("amount", 4).isEmpty() }
      }

      it("should be ok with number index.") {
        PostStr.findBy("amount", 3).delete()
        val b = PostStr.findBy("name", "B")
        val c = PostStr.findBy("name", "C")
        val d = PostStr.findBy("name", "D")
        assertTrue { PostStr.findBy("amount", 3).isEmpty() }
        assertTrue { b.isEmpty() }
        assertTrue { c.isEmpty() }
        assertTrue { d.isEmpty() }
      }
    }
  }
})