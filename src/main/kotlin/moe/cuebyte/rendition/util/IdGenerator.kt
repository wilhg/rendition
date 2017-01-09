package moe.cuebyte.rendition.util

import java.math.BigInteger
import java.security.SecureRandom

internal object IdGenerator {
  private val rnd = SecureRandom()
  fun next(length: Int = 16): String = BigInteger(130, rnd).toString(length)
}