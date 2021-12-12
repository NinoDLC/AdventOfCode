package year2021

import utils.logMeasureTime
import java.io.File

class Day0 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day0.txt").readLines()

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