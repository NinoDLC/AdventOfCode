package year2015

import utils.getPuzzleInput
import utils.logMeasureTime

class Day25 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day25().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day25().partTwo(lines)
            }
            println()
        }

        @Suppress("RegExpRepeatedSpace")
        private val ROW_COLUMN_REGEX =
            "To continue, please consult the code grid in the manual.  Enter the code at row ([0-9]+), column ([0-9]+).".toRegex()
    }

    private fun partOne(lines: List<String>) {
        val (x, y) = getTargetRowAndColumn(lines)

        var currentValue = 20151125L
        var currentX = 1
        var currentY = 1

        while (currentX != x || currentY != y) {
            currentValue = (currentValue * 252533) % 33554393
            if (currentY == 1) {
                currentY = currentX + 1
                currentX = 1
            } else {
                currentY--
                currentX++
            }
        }

        println(currentValue)
    }

    private fun partTwo(lines: List<String>) {
        println(lines)
    }

    private fun getTargetRowAndColumn(lines: List<String>): Pair<Int, Int> =
        ROW_COLUMN_REGEX.find(lines.first())!!.destructured.toList().map { it.toInt() }.let {
            it.component2() to it.component1()
        }
}