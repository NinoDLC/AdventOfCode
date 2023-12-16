package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day01Imperative {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day01Imperative().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day01Imperative().partTwo(lines)
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
        var maxCalories1 = 0
        var maxCalories2 = 0
        var maxCalories3 = 0
        var currentCalories = 0

        for (line in lines) {
            if (line.isEmpty()) {
                when {
                    currentCalories > maxCalories1 -> {
                        maxCalories3 = maxCalories2
                        maxCalories2 = maxCalories1
                        maxCalories1 = currentCalories
                    }

                    currentCalories > maxCalories2 -> {
                        maxCalories3 = maxCalories2
                        maxCalories2 = currentCalories
                    }

                    currentCalories > maxCalories3 -> maxCalories3 = currentCalories
                }

                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }

        println(maxCalories1 + maxCalories2 + maxCalories3)
    }
}