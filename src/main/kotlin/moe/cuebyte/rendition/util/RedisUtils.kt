package moe.cuebyte.rendition.util

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model

internal fun genId(m: Model, v: String): String = "${m.name}:$v"
internal fun genKey(m: Model, c: Column) = "${m.name}:${c.name}"
internal fun genKey(m: Model, c: Column, v: String) = "${m.name}:${c.name}:$v"
internal fun genKey(m: Model, name: String) = "${m.name}:$name"
internal fun genKey(m: Model, name: String, v: String) = "${m.name}:$name:$v"