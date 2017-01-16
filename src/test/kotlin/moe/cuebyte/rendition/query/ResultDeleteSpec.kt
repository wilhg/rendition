package moe.cuebyte.rendition.query

import moe.cuebyte.rendition.mock.PostAuto
import moe.cuebyte.rendition.mock.PostStr
import moe.cuebyte.rendition.util.Connection
import moe.cuebyte.rendition.util.genKey
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import redis.clients.jedis.Jedis
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

object ResultDeleteSpec : Spek({
  describe("ResultDeleteSpec") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }

    on("delete result") {
      val id = PostStr.insert {
        it["id"] = "A"
        it["name"] = "A"
        it["amount"] = 200
      }
      it("delete result") {
        assertTrue { PostStr.find(id!!).delete() }

        val t = Connection.get().multi()
        val s = t.sismember(genKey(PostStr, "name", "A"), id)
        val z = t.zscore(genKey(PostStr, "amount"), id)
        t.exec()
        assertFalse { s.get() }
        assertNull(z.get())
      }
    }
  }
})