package moe.cuebyte.rendition

import moe.cuebyte.rendition.type.string
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object StringID : Model("Book", {
  it["id"] = string().primaryKey().auto()
})

object ModelSpec : Spek({

  describe("insert") {}

  describe("batch insert") {}
})