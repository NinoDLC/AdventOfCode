package year2022

import utils.logMeasureTime
import java.io.File

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2022/day01.txt").readLines()

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
        val elvesCalories: List<Int> = getElvesCalories(lines)

        println(elvesCalories.max())
    }

    private fun partTwo(lines: List<String>) {
        val elvesCalories: List<Int> = getElvesCalories(lines)

        println(elvesCalories.sortedDescending().take(3).sum())
    }

    private fun getElvesCalories(lines: List<String>): List<Int> {
        val caloriesGrouped = mutableListOf(mutableListOf<String>())

        lines.forEach { line ->
            if (line.isEmpty()) {
                caloriesGrouped.add(mutableListOf())
            } else {
                val calories = caloriesGrouped.last()
                calories.add(line)
            }
        }

        return  caloriesGrouped.map { calories ->
            calories.sumOf { it.toInt() }
        }
    }
}