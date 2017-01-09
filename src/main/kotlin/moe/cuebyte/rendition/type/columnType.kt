package moe.cuebyte.rendition.type

import moe.cuebyte.rendition.IncompleteColumn

fun bool(default: Boolean = false) = IncompleteColumn(Boolean::class.java, default)
fun string(default: String = "") = IncompleteColumn(String::class.java, default)
fun int(default: Int = 0) = IncompleteColumn(Int::class.java, default)
fun long(default: Long = 0) = IncompleteColumn(Long::class.java, default)
fun float(default: Float = 0f) = IncompleteColumn(Float::class.java, default)
fun double(default: Double = 0.0) = IncompleteColumn(Double::class.java, default)