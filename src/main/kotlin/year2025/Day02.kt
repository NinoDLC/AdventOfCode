package year2025

import utils.getPuzzleInput
import utils.logMeasureTime

class Day02 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day02().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day02().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            getInvalidIdsCount(
                lines = lines,
                regex = Regex("(\\d+)\\1"),
            )
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            getInvalidIdsCount(
                lines = lines,
                regex = Regex("(\\d+)\\1+"),
            )
        )
    }

    private fun getInvalidIdsCount(
        lines: List<String>,
        regex: Regex,
    ): Long = lines
        .first()
        .split(",")
        .fold(0L) { acc, line ->
            val (startAsString, endAsString) = line.split("-")
            val start = startAsString.toLong()
            val end = endAsString.toLong()

            acc + sumInvalidIds(start, end, regex)
        }

    private fun sumInvalidIds(
        start: Long,
        end: Long,
        regex: Regex,
    ): Long = (start..end).fold(0L) { acc, id ->
        if (regex.matches(id.toString())) {
            acc + id
        } else {
            acc
        }
    }
}
