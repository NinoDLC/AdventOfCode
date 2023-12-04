package year2023

import utils.getPuzzleInput
import utils.logMeasureTime
import kotlin.math.pow

class Day04 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day04().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day04().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            getMatchingWinningNumberCounts(lines).sumOf { matchingWinningNumberCount ->
                2.0.pow((matchingWinningNumberCount - 1).toDouble()).toInt()
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val matchingWinningNumberCounts = getMatchingWinningNumberCounts(lines)
        val cardCounts = Array(matchingWinningNumberCounts.size) { 1 }

        for (index in matchingWinningNumberCounts.indices) {
            val matchingWinningNumberCount = matchingWinningNumberCounts[index]

            for (nextCardIndex in index + 1..index + matchingWinningNumberCount) {
                cardCounts[nextCardIndex] += cardCounts[index]
            }
        }

        println(cardCounts.sum())
    }

    private fun getMatchingWinningNumberCounts(lines: List<String>): List<Int> = lines.map { line ->
        val numbers = line.split(":")[1].split("|")
        val winningNumbers = numbers.first().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val guessedNumbers = numbers[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        guessedNumbers.count { it in winningNumbers }
    }
}