package year2021

import utils.logMeasureTime
import java.io.File
import kotlin.math.abs

class Day07 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day07.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day07().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day07().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val values = lines.first().split(",").map { it.toInt() }

        val min = values.minOrNull() ?: throw IllegalStateException("No min found ??")
        val max = values.maxOrNull() ?: throw IllegalStateException("No max found ??")

        // Index / fuel
        val results = mutableMapOf<Int, Int>()

        for (i in min..max) {
            results[i] = values.fold(0) { acc, position ->
                acc + abs(i - position)
            }
        }

        println(results.minOf { it.value })
    }

    private fun partTwo(lines: List<String>) {
        val values = lines.first().split(",").map { it.toInt() }

        val min = values.minOrNull() ?: throw IllegalStateException("No min found ??")
        val max = values.maxOrNull() ?: throw IllegalStateException("No max found ??")

        // Index / fuel
        val results = mutableMapOf<Int, Int>()

        for (i in min..max) {
            results[i] = values.fold(0) { acc, position ->
                val distance = abs(i - position)
                acc + ((distance * (distance + 1)) / 2)
            }
        }

        println(results.minOf { it.value })
    }
}