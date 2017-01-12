package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object IncompleteColumnSpec : Spek({
  describe("incomplete column") {
    on("set type") {
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
    }

    on("methods") {
      it("should be ok with complete") {
        val col = int().complete("name")
        assertTrue(col is Column)
        assertEquals(col.name, "name")
      }
    }

    on("exception check") {
      it("should throw exception when") {
        assertFails { int().primaryKey().index() }
        assertFails { int().index().primaryKey() }
        assertFails { int().index().auto() }
        assertFails { int().auto() }
        assertFails { bool().primaryKey() }
        assertFails { bool().index() }
      }
    }
  }
})

object ColumnSpec : Spek({
  describe("columns") {
    on("type meta") {
      fun info(x: IncompleteColumn) = x.complete("").meta

      it("'s normal column should be correct") {
        assertEquals(info(int()), Column.Meta.NONE)
        assertEquals(info(long()), Column.Meta.NONE)
        assertEquals(info(float()), Column.Meta.NONE)
        assertEquals(info(double()), Column.Meta.NONE)
        assertEquals(info(string()), Column.Meta.NONE)
      }
      it("'s primary key should be correct") {
        assertEquals(info(int().primaryKey()), Column.Meta.NUMBER_PK)
        assertEquals(info(long().primaryKey()), Column.Meta.NUMBER_PK)
        assertEquals(info(float().primaryKey()), Column.Meta.NUMBER_PK)
        assertEquals(info(double().primaryKey()), Column.Meta.NUMBER_PK)
        assertEquals(info(string().primaryKey()), Column.Meta.STRING_PK)
      }
      it("'s index should be correct") {
        assertEquals(info(int().index()), Column.Meta.NUMBER_INDEX)
        assertEquals(info(long().index()), Column.Meta.NUMBER_INDEX)
        assertEquals(info(float().index()), Column.Meta.NUMBER_INDEX)
        assertEquals(info(double().index()), Column.Meta.NUMBER_INDEX)
        assertEquals(info(string().index()), Column.Meta.STRING_INDEX)
      }

      it("should be ok with auto") {
        assertTrue { string().primaryKey().auto().complete("").automated }

        assertFails { int().primaryKey().auto().complete("").automated }

        assertFalse { string().primaryKey().complete("").automated }
        assertFalse { string().index().complete("").automated }
        assertFalse { string().complete("").automated }

        assertFalse { int().primaryKey().complete("").automated }
        assertFalse { int().index().complete("").automated }
        assertFalse { int().complete("").automated }
      }
    }
    on("methods") {
      it("should be ok with checkType") {
        assertTrue(int().complete("").checkType(0))
        assertTrue(long().complete("").checkType(0L))
        assertTrue(float().complete("").checkType(0f))
        assertTrue(double().complete("").checkType(0.0))
        assertTrue(string().complete("").checkType("喵"))
        assertFalse(string().complete("").checkType(BigDecimal(1)))
      }
    }
  }
})