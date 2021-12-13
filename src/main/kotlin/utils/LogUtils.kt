package utils

import java.lang.IllegalStateException

fun print2DPositions(
    positions: Collection<Pair<Int, Int>>,
    exhaustive: Boolean = false
) {
    print2DPositionsWithSign(
        positions = positions.map {
            Triple(it.first, it.second, '#')
        },
        exhaustive = exhaustive
    )
}

fun print2DPositionsWithSign(
    positions: Collection<Triple<Int, Int, Any>>,
    exhaustive: Boolean = false
) {
    val stringBuilder = StringBuilder()

    val xMax = positions.maxOf { it.first }
    val yMax = positions.maxOf { it.second }

    for (y in 0..yMax) {
        for (x in 0..xMax) {
            val existing = positions.find {
                it.first == x && it.second == y
            }

            if (existing == null) {
                if (exhaustive) {
                    throw IllegalStateException("Position x=$x,y=$y does not exist in the collection !")
                } else {
                    stringBuilder.append('.')
                }
            } else {
                stringBuilder.append(existing.third)
            }
        }
        stringBuilder.append("\n")
    }
    println(stringBuilder.toString())
}