package moe.cuebyte.rendition.util

import redis.clients.jedis.*


object Connection {

  private var conn: Jedis? = null
  private var pool: JedisPool? = null

  fun set(jedis: Jedis) {
    if (conn != null || this.pool != null) {
      throw Exception("Connection has been set.")
    }
    conn = jedis
  }

  fun set(pool: JedisPool) {
    if (conn != null || this.pool != null) {
      throw Exception("Connection has been set.")
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
