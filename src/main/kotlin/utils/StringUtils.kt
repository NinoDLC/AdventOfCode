package utils

/**
 * Centers (with spaces around) the given String inside a new String with a width (Char count) of [maxWidth].
 *
 * ```
 * "nino".center(10) -> "   nino   "
 * "nino".center(4) -> "nino"
 * "nino".center(3) -> IllegalArgumentException
 * ```
 *
 * @param maxWidth must be equal or higher than String's length
 */
fun String.center(maxWidth: Int): String {
    val spaceSize = maxWidth - this.length
    require(spaceSize >= 0) {
        "Text [$this] with a length of [${this.length}] is longer than maxWidth [$maxWidth]"
    }
    return " ".repeat((spaceSize + 1) / 2) + this + " ".repeat(spaceSize / 2)
}