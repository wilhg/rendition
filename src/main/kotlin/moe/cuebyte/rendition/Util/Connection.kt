package moe.cuebyte.rendition.Util

import redis.clients.jedis.*


internal object Connection {
  private var conn: Jedis? = null
  private var pool: JedisPool? = null

  fun init(connection: Jedis) {
    if (conn != null || this.pool != null) throw Exception("Connection has been set.")
    conn = connection
  }

  fun init(pool: JedisPool) {
    if (conn != null || this.pool != null) throw Exception("Connection has been set.")
    this.pool = pool
  }

  fun get(): Jedis {
    if (conn != null) return conn!!
    if (pool != null) return pool!!.getResource()
    throw Exception("The connection has not been initialized.")
  }
}
