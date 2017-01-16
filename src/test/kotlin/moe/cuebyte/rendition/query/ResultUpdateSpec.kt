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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object ResultUpdateSpec : Spek({
  describe("ResultUpdateSpec") {
    beforeGroup {
      Connection.set(Jedis("localhost"))
      Connection.get().select(4)
    }
    afterGroup {
      Connection.get().flushDB()
    }

    on("update result") {
      val id = PostStr.insert {
        it["id"] = "A"
        it["name"] = "A"
        it["amount"] = 200
      }
      it("update result") {
        PostStr.find(id!!).update { it["name"] = "B" }

        val t = Connection.get().multi()
        val s1 = t.sismember(genKey(PostStr, "name", "A"), id)
        val s2 = t.sismember(genKey(PostStr, "name", "B"), id)
        t.exec()
        assertFalse { s1.get() }
        assertTrue { s2.get() }
      }

      it("update result") {
        PostAuto.find(id!!).update { it["amount"] = 100 }

        val t = Connection.get().multi()
        val z = t.zscore(genKey(PostAuto, "amount"), id)
        t.exec()
        assertEquals(z.get(), 100.0)
      }
    }
  }
})