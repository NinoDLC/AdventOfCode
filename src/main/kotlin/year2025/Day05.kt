package year2025

import utils.getPuzzleInput
import utils.logMeasureTime

class Day05 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day05().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day05().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val splitIndex = lines.indexOf("")

        val freshRanges = lines.subList(0, splitIndex).map {
            LongRange(
                start = it.split("-").first().toLong(),
                endInclusive = it.split("-").last().toLong(),
            )
        }

        val ingredientIds = lines.subList(splitIndex + 1, lines.size)

        println(
            ingredientIds.count { ingredientId ->
                freshRanges.any { freshRange ->
                    ingredientId.toLong() in freshRange
                }
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val splitIndex = lines.indexOf("")

        val freshRanges = lines.subList(0, splitIndex).map {
            LongRange(
                start = it.split("-").first().toLong(),
                endInclusive = it.split("-").last().toLong(),
            )
        }.sortedBy { it.first }

        var freshIdsCount = 0L
        var currentId = 0L

        freshRanges.forEach { freshRange ->
            if (currentId < freshRange.last) {
                freshIdsCount += freshRange.last - maxOf(currentId, freshRange.first - 1)
                currentId = freshRange.last
            }
        }

        println(freshIdsCount)
    }
}
