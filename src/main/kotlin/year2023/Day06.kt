package year2023

import utils.getPuzzleInput
import utils.logMeasureTime

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
        val parseNumbersInLine = fun(line: String) = line
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toLong() }

        val times = parseNumbersInLine(lines[0])
        val distances = parseNumbersInLine(lines[1])

        println(
            times.mapIndexed { index, time ->
                RaceRecord(
                    time = time,
                    distance = distances[index],
                )
            }.fold(1) { acc, record ->
                acc * countPossibleWinningStrategies(record)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val parseNumberInLine = fun(line: String) = line
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(separator = "")
            .toLong()

        val time = parseNumberInLine(lines[0])
        val distance = parseNumberInLine(lines[1])

        println(
            countPossibleWinningStrategies(
                RaceRecord(
                    time = time,
                    distance = distance,
                )
            )
        )
    }

    private fun countPossibleWinningStrategies(record: RaceRecord): Int = (0..record.time / 2).map { possibleButtonHoldingTime ->
        possibleButtonHoldingTime * (record.time - possibleButtonHoldingTime)
    }.count { distance ->
        distance > record.distance
    }.let { halvedPossibleWinningStrategiesCount ->
        if (record.time % 2 == 0L) {
            halvedPossibleWinningStrategiesCount * 2 - 1
        } else {
            halvedPossibleWinningStrategiesCount * 2
        }
    }

    private data class RaceRecord(
        val time: Long,
        val distance: Long,
    )
}