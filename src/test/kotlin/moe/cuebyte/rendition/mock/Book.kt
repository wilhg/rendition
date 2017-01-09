package moe.cuebyte.rendition.mock

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string

object Book : Model("Book", {
  it["id"] = string().primaryKey().auto()
  it["author"] = string().index()
  it["publish"] = string().index()
  it["words"] = long().index()
  it["sales"] = long().index()
  it["introduce"] = string()
  it["date"] = string()
})