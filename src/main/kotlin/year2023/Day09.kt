package year2023

import utils.getPuzzleInput
import utils.logMeasureTime

class Day09 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day09().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day09().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val sequences: List<List<Long>> = parseSequences(lines)

        println(
            sequences.sumOf { sequence ->
                getLastExtrapolatedValue(sequence)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val sequences: List<List<Long>> = parseSequences(lines)

        println(
            sequences.sumOf { sequence ->
                getFirstExtrapolatedValue(sequence)
            }
        )
    }

    private fun parseSequences(lines: List<String>): List<List<Long>> = lines.map { line ->
        line.split(" ").map {
            it.toLong()
        }
    }

    private fun getLastExtrapolatedValue(sequence: List<Long>): Long {
        val newSequence = sequence.zipWithNext { current, next ->
            next - current
        }

        return if (newSequence.all { it == 0L }) {
            sequence.last()
        } else {
            sequence.last() + getLastExtrapolatedValue(newSequence)
        }
    }

    private fun getFirstExtrapolatedValue(sequence: List<Long>): Long {
        val newSequence = sequence.zipWithNext { current, next ->
            next - current
        }

        return if (newSequence.all { it == 0L }) {
            sequence.first()
        } else {
            sequence.first() - getFirstExtrapolatedValue(newSequence)
        }
    }
}