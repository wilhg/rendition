package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object StringID : Model("Book", {
  it["id"] = string().primaryKey().auto()
})

object ModelSpec : Spek({
  describe("id init") {
    on("string id") {
      it("") {
        assert(StringID.pk.info == Column.Info.STRING_PK)
        assert(StringID.pk.automated)
      }
    }
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