package moe.cuebyte.rendition.util

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object Connection {

  private var conn: Jedis? = null
  private var pool: JedisPool? = null

  fun set(jedis: Jedis) {
    if (conn != null) {
      return
    }
    conn = jedis
  }

  fun set(pool: JedisPool) {
    if (this.pool != null) {
      return
    }
    this.pool = pool
  }

  fun get(): Jedis {
    if (pool != null) {
      return pool!!.resource
    }
    if (conn != null) {
      return conn!!
    }
    throw Exception("The connection has not been initialized.")
  }
}