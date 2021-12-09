package year2021

import utils.logMeasureTime
import java.io.File

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day01.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day01().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day01().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        var increases = 0

        val mapped = lines.map {
            it.toInt()
        }

        mapped.forEachIndexed { index, line ->
            val previous = mapped.getOrNull(index - 1) ?: return@forEachIndexed

            if (line > previous) {
                increases++
            }
        }

        println(increases)
    }

    private fun partTwo(lines: List<String>) {
        var increases = 0

        val mapped = lines.map {
            it.toInt()
        }

        mapped.forEachIndexed { index, current ->
            val previous = mapped.getOrNull(index - 1) ?: return@forEachIndexed
            val previousTwice = mapped.getOrNull(index - 2) ?: return@forEachIndexed
            val previousThird = mapped.getOrNull(index - 3) ?: return@forEachIndexed

            if ((current + previous + previousTwice) > (previous + previousTwice + previousThird)) {
                increases++
            }
        }

        println(increases)
    }
}