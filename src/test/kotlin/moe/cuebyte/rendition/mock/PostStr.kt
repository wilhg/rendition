package moe.cuebyte.rendition.mock

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.string

object PostStr : Model("post_str", {
  it["id"] = string().primaryKey()
  it["name"] = string().index()
  it["amount"] = int().index()
})

object PostAuto : Model("post_auto", {
  it["id"] = string().primaryKey().auto()
  it["name"] = string().index()
  it["amount"] = int().index()
})