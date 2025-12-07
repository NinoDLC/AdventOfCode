package year2025

import utils.getPuzzleInput
import utils.logMeasureTime
import kotlin.math.pow

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
        val indexedProblemValuesListAsString = lines.dropLast(1)
        val operatorsAsString = lines.last()

        val indexedProblemValues: List<List<Int>> = indexedProblemValuesListAsString.map { indexedProblemValuesAsString ->
            indexedProblemValuesAsString
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
        }

        val operators = operatorsAsString
            .split(" ")
            .filter { it.isNotBlank() }

        check(indexedProblemValues.all { it.size == operators.size })

        println(
            operators.mapIndexed { index, char ->
                when (char) {
                    "+" -> indexedProblemValues.fold(0L) { acc, indexedProblemValue ->
                        acc + indexedProblemValue[index]
                    }

                    "*" -> indexedProblemValues.fold(1L) { acc, indexedProblemValue ->
                        acc * indexedProblemValue[index]
                    }

                    else -> {
                        throw IllegalStateException("Unknown operator = $char")
                    }
                }
            }.sum()
        )
    }

    private fun partTwo(lines: List<String>) {
        val rowsAsString = lines.dropLast(1)
        val operators = lines.last().split(" ").filter { it.isNotBlank() }

        val rowsPerProblem = rowsAsString.size
        val problemsCount = operators.size

        var cursorIndex = 0
        val problemsNumbers: List<List<String>> = List(problemsCount) {
            val maxNumberLength = rowsAsString.maxOf { row ->
                row
                    .substring(cursorIndex)
                    .indexOfFirst { it == ' ' }
                    .takeIf { it != -1 }
                    ?: (row.length - cursorIndex)
            }

            List(rowsPerProblem) { rowIndex ->
                val row = rowsAsString[rowIndex]

                row
                    // The space chars at the end of day06.txt are disappearing when I compile. I suspect Windows. Or IntelliJ.
                    // So we need to coerce the value to the max length of the String...
                    .substring(cursorIndex, (cursorIndex + maxNumberLength).coerceAtMost(row.length))
                    // ... and pad to "bring back" the spaces -_-
                    .padEnd(maxNumberLength)
            }.also {
                cursorIndex += maxNumberLength + 1
            }
        }

        val problemNumbersAsCephalopodStyle: List<List<Long>> = problemsNumbers.map { problemNumbers ->
            val numberOfDigits = problemNumbers.maxOf { it.length }

            List(numberOfDigits) { index ->
                val numbers = problemNumbers.mapNotNull { it[index].digitToIntOrNull() }

                numbers.foldIndexed(0L) { index, acc, digit ->
                    acc + (digit * 10.0.pow(numbers.size - index - 1)).toLong()
                }
            }
        }

        println(
            operators.mapIndexed { index, char ->
                when (char) {
                    "+" -> problemNumbersAsCephalopodStyle[index].fold(0L) { acc, problemNumber ->
                        acc + problemNumber
                    }

                    "*" -> problemNumbersAsCephalopodStyle[index].fold(1L) { acc, problemNumber ->
                        acc * problemNumber
                    }

                    else -> {
                        throw IllegalStateException("Unknown operator = $char")
                    }
                }
            }.sum()
        )
    }
}
