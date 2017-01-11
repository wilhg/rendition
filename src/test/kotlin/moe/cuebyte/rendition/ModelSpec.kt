package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ModelSpec : Spek({
  describe("model property") {
    on("basic") {
    }
  }
})


object Book : Model("book", {
  it["id"] = string().primaryKey().auto()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})