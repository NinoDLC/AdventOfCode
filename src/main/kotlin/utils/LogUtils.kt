package utils

fun print2DPositions(positions: List<Triple<Int, Int, Any>>) {
    val stringBuilder = StringBuilder()

    val xMax = positions.maxOf { it.first }
    val yMax = positions.maxOf { it.second }

    for (y in 0..yMax) {
        for (x in 0..xMax) {
            val existing = positions.first {
                it.first == x && it.second == y
            }

            stringBuilder.append(existing.third)
        }
        stringBuilder.append("\n")
    }
    println(stringBuilder.toString())
}