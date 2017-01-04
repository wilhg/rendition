package moe.cuebyte.rendition.Util

import java.math.BigInteger
import java.security.SecureRandom

object IdGenerator {
  private val rnd = SecureRandom()
  fun next(length: Int = 16): String = BigInteger(130, rnd).toString(length)
}