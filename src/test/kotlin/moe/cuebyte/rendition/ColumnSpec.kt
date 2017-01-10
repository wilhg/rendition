package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

object IncompleteColumnSpec : Spek({
  describe("incomplete column") {
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
    it("should be string type") {
      assertEquals(string().type, String::class.java)
      assertEquals(string().default, "")
    }

    it("should be ok with complete") {
      val col = int().complete("name")
      assertTrue(col is Column)
      assertEquals(col.name, "name")
    }

    it("should throw exception when") {
      assertFails { int().primaryKey().index() }
      assertFails { int().index().primaryKey() }
      assertFails { int().index().auto() }
      assertFails { int().auto() }
      assertFails { bool().primaryKey() }
      assertFails { bool().index() }
    }
  }
})

object ColumnSpec : Spek({
  describe("columns") {
    it("should be ok with checkType") {
      assertTrue(int().complete("").checkType(0))
      assertTrue(long().complete("").checkType(0L))
      assertTrue(float().complete("").checkType(0f))
      assertTrue(double().complete("").checkType(0.0))
      assertTrue(string().complete("").checkType("喵"))
    }
  }
})