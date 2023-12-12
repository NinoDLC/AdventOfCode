package utils

fun Int.isTrueAtBitPosition(bitPosition: Int): Boolean {
    val acceptableRange = 0 until 31
    require(bitPosition in acceptableRange) {
        "Incorrect bitPosition $bitPosition, it should be in the range $acceptableRange"
    }

    return ((this shr bitPosition) and 1) == 1
}

fun Long.isTrueAtBitPosition(bitPosition: Int): Boolean {
    val acceptableRange = 0 until 63
    require(bitPosition in acceptableRange) {
        "Incorrect bitPosition $bitPosition, it should be in the range $acceptableRange"
    }

    return ((this shr bitPosition) and 1) == 1L
}