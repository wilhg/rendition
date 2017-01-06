package moe.cuebyte.rendition.Util

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model

internal fun genId(m: Model, v: String): String = "${m.name}:$v"
internal fun genId(m: Model, v: Number): String = "${m.name}:${v.toString()}"

internal fun genKey(m: Model, c: Column) = "${m.name}:${c.name}"
internal fun genKey(m: Model, c: Column, v: String) = "${m.name}:${c.name}:$v"
internal fun genKey(m: Model, c: Column, v: Number) = "${m.name}:${c.name}:${v.toString()}"
