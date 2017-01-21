package moe.cuebyte.rendition

import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.query.method.*
import moe.cuebyte.rendition.util.Connection
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import kotlin.test.assertEquals

object CalculatorSpec : Spek({
  describe("CalculatorSpec") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }

    on("basic") {
      PostStr.batchInsert(listOf(
          mapOf("id" to "1", "name" to "a", "amount" to 2),
          mapOf("id" to "2", "name" to "a", "amount" to 1),
          mapOf("id" to "3", "name" to "b", "amount" to 1)
      ))

      it("AND") {
        val resultSet = PostStr.query { findBy("name", "a") AND findBy("amount", 1) }
        assertEquals(resultSet.first()["id"], "2")
      }

      it("OR") {
        val resultSet = PostStr.query { findBy("name", "a") OR findBy("amount", 1) }
        assertEquals(resultSet.size, 3)
      }
    }
  }
})