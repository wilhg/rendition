package moe.cuebyte.rendition

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on

object ModelSpec : Spek({
  describe("id init") {
    on("string id") {}
    on("automated") {}
    on("number id") {}
  }

  describe("index init") {
    on("string index") {}
    on("number index") {}
  }

  describe("data init") {}

  describe("insert") {}

  describe("batch insert") {}
})