package year2022

import utils.getPuzzleInput
import utils.logMeasureTime
import java.io.File

class Day01Hybrid {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day01Hybrid().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day01Hybrid().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        var maxCalories = 0
        var currentCalories = 0

        for (line in lines) {
            if (line.isEmpty()) {
                if (currentCalories > maxCalories) {
                    maxCalories = currentCalories
                }
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }

        println(maxCalories)
    }

    private fun partTwo(lines: List<String>) {
        val calories = mutableListOf<Int>()
        var currentCalories = 0

        for (line in lines) {
            if (line.isEmpty()) {
                calories.add(currentCalories)
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }

        println(calories.sortedDescending().take(3).sum())
    }
}