package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day06 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day06().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day06().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val line = lines.first()
        line.onEachIndexed { index, _ ->
            if (index >= 3 && line.substring(index - 3, index + 1).toSet().size == 4) {
                println(index + 1)
                return
            }
        }
    }

    private fun partTwo(lines: List<String>) {
        val line = lines.first()
        line.onEachIndexed { index, _ ->
            if (index >= 13 && line.substring(index - 13, index + 1).toSet().size == 14) {
                println(index + 1)
                return
            }
        }
    }
}