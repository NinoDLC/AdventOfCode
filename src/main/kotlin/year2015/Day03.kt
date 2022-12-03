package year2015

import utils.logMeasureTime
import java.io.File

class Day03 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2015/day03.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day03().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day03().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.first().fold(listOf(Position(0, 0))) { acc, char ->
                val previousPosition = acc.last()
                val (deltaX, deltaY) = getMovementFromChar(char)
                acc + Position(previousPosition.x + deltaX, previousPosition.y + deltaY)
            }.groupingBy { it }
                .eachCount()
                .filter { it.value >= 1 }
                .count()
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.first().fold(listOf(Position(0, 0), Position(0, 0))) { acc, char ->
                val previousPosition = acc[acc.size - 2]
                val (deltaX, deltaY) = getMovementFromChar(char)
                acc + Position(previousPosition.x + deltaX, previousPosition.y + deltaY)
            }.groupingBy { it }
                .eachCount()
                .filter { it.value >= 1 }
                .count()
        )
    }

    private fun getMovementFromChar(char: Char): Pair<Int, Int> = when (char) {
        '^' -> 0 to -1
        '>' -> 1 to 0
        'v' -> 0 to 1
        '<' -> -1 to 0
        else -> throw IllegalStateException("Unknown char: $char")
    }

    data class Position(
        val x: Int,
        val y: Int,
    )
}