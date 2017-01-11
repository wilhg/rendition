package moe.cuebyte.rendition.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import kotlin.test.assertFails

object ConnectionSpec : Spek({
  describe("Connection") {
    on("set connection") {
      val jedis = Jedis("localhost")
      val pool = JedisPool(JedisPoolConfig(), "localhost")
      it("should be useful") {
        assertFails { Connection.get() }
        Connection.set(pool)
        assert(Connection.get().ping() == "PONG")
      }
    }
  }
})