package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day0 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day0().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day0().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(lines)
    }

    private fun partTwo(lines: List<String>) {
        println(lines)
    }
}