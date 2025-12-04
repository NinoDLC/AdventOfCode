package year2025

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime

class Day04 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day04().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day04().partTwo(lines)
            }
            println()
        }
    }

private fun partOne(lines: List<String>) {
    val plane = Plane.of(lines)

    println(
        plane.count { itemPosition ->
            itemPosition.item == '@' && itemPosition.position.neighboursSquare
                .map { plane.getOrNull(it) }
                .filter { it.item == '@' }
                .size < 4
        }
    )
}

private fun partTwo(lines: List<String>) {
    val plane = Plane.of(lines)
    var removedRollsOfPaper = 0L
    var lastCount: Long? = null

    while (lastCount != removedRollsOfPaper) {
        lastCount = removedRollsOfPaper

        plane.forEach { itemPosition ->
            val canBeRemoved = itemPosition.item == '@' && itemPosition.position.neighboursSquare
                .map { plane.getOrNull(it) }
                .filter { it.item == '@' }
                .size < 4

            if (canBeRemoved) {
                removedRollsOfPaper++
                plane[itemPosition.position] = '.'
            }
        }
    }

    println(removedRollsOfPaper)
}
}
