package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ColumnSpec : Spek({
  describe("incomplete column") {
    on("set type") {
      it("should be string type") {
        assertEquals(string().type, String::class.java)
        assertEquals(string().default, "")
      }
      it("should be int type") {
        assertEquals(int().type, Int::class.java)
        assertEquals(int().default, 0)
      }
      it("should be long type") {
        assertEquals(long().type, Long::class.java)
        assertEquals(long().default, 0L)
      }
      it("should be float type") {
        assertEquals(float().type, Float::class.java)
        assertEquals(float().default, 0f)
      }
      it("should be double type") {
        assertEquals(double().type, Double::class.java)
        assertEquals(double().default, 0.0)
      }
    }

    it("should be ok with complete") {
      assertTrue(int().complete("") is Column)
    }

    it("should be ok with checkType") {
      assertTrue(int().checkType(0))
      assertTrue(long().checkType(0L))
      assertTrue(float().checkType(0f))
      assertTrue(double().checkType(0.0))
      assertTrue(string().checkType("猫"))
    }
  }
  describe("id init") {
    it("string id") {
    }
    on("automated") {}
    on("number id") {}
  }

  describe("index init") {
    on("string index") {}
    on("number index") {}
  }

  describe("data init") {}
})