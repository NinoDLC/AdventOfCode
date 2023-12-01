package year2023

import utils.getPuzzleInput
import utils.logMeasureTime

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
        println(
            lines.fold(0) { acc, line ->
                val firstNumber: Int = line.firstNotNullOf { it.digitToIntOrNull() }
                val lastNumber: Int = line.reversed().firstNotNullOf { it.digitToIntOrNull() }
                val finalNumber: Int = firstNumber * 10 + lastNumber

                acc + finalNumber
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.fold(0) { acc, line ->
                val groups: List<String> = line.indices.map {
                    line.substring(it, (it + 5).coerceAtMost(line.length))
                }

                val firstNumber: Int = groups.firstNotNullOf { group ->
                    group.first().digitToIntOrNull() ?: getAsWrittenNumber(group)
                }

                val lastNumber: Int = groups.reversed().firstNotNullOf { group ->
                    group.first().digitToIntOrNull() ?: getAsWrittenNumber(group)
                }

                acc + firstNumber * 10 + lastNumber
            }
        )
    }

    private fun getAsWrittenNumber(group: String): Int? = if (group.startsWith("one")) {
        1
    } else if (group.startsWith("two")) {
        2
    } else if (group.startsWith("three")) {
        3
    } else if (group.startsWith("four")) {
        4
    } else if (group.startsWith("five")) {
        5
    } else if (group.startsWith("six")) {
        6
    } else if (group.startsWith("seven")) {
        7
    } else if (group.startsWith("eight")) {
        8
    } else if (group.startsWith("nine")) {
        9
    } else {
        null
    }
}