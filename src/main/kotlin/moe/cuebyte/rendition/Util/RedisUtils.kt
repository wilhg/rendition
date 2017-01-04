package moe.cuebyte.rendition.Util

import moe.cuebyte.rendition.Column
import moe.cuebyte.rendition.Model

fun genId(m: Model, v: String): String = "${m.name}:$v"
fun genId(m: Model, v: Number): String = "${m.name}:${v.toString()}"

fun genField(m: Model, c: Column) = "${m.name}:${c.name}"
fun genField(m: Model, c: Column, v: String) = "${m.name}:${c.name}:$v"
fun genField(m: Model, c: Column, v: Number) = "${m.name}:${c.name}:${v.toString()}"
