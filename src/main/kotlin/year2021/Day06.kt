package year2021

import utils.logMeasureTime
import java.io.File

class Day06 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day06.txt").readLines()

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
        // Days old, count
        var states = HashMap<Int, Int>()

        lines.first().split(",").map { it.toInt() }.forEach { daysOld ->
            states[daysOld] = states.getOrDefault(daysOld, 0) + 1
        }

        for (i in 0..79) {
            val newStates = HashMap<Int, Int>()

            newStates[0] = states[1] ?: 0
            newStates[1] = states[2] ?: 0
            newStates[2] = states[3] ?: 0
            newStates[3] = states[4] ?: 0
            newStates[4] = states[5] ?: 0
            newStates[5] = states[6] ?: 0
            newStates[6] = (states[7] ?: 0) + (states[0] ?: 0)
            newStates[7] = states[8] ?: 0
            newStates[8] = states[0] ?: 0

            states = newStates
        }

        println(
            states.values.fold(0) { previous, current ->
                previous + current
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        // Days old, count
        var states = HashMap<Long, Long>()

        lines.first().split(",").map { it.toLong() }.forEach { daysOld ->
            states[daysOld] = states.getOrDefault(daysOld, 0) + 1
        }

        for (i in 0..255) {
            val newStates = HashMap<Long, Long>()

            newStates[0] = states[1] ?: 0
            newStates[1] = states[2] ?: 0
            newStates[2] = states[3] ?: 0
            newStates[3] = states[4] ?: 0
            newStates[4] = states[5] ?: 0
            newStates[5] = states[6] ?: 0
            newStates[6] = (states[7] ?: 0) + (states[0] ?: 0)
            newStates[7] = states[8] ?: 0
            newStates[8] = states[0] ?: 0

            states = newStates
        }

        println(
            states.values.fold(0L) { previous, current ->
                previous + current
            }
        )
    }
}