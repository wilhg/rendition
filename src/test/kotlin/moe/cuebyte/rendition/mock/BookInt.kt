package moe.cuebyte.rendition.mock

import moe.cuebyte.rendition.Model
import moe.cuebyte.rendition.type.int
import moe.cuebyte.rendition.type.long
import moe.cuebyte.rendition.type.string

object BookInt : Model("book_int", mapOf(
    "id" to int().primaryKey(),
    "author" to string().index(),
    "publish" to string().index(),
    "words" to long().index(),
    "sales" to long().index(),
    "introduce" to string(),
    "date" to string()
))