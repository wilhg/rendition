package moe.cuebyte.rendition

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ColumnSpec : Spek({
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