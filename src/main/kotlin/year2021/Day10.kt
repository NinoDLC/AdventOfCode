package year2021

import utils.logMeasureTime
import java.io.File

class Day10 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day10.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day10().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day10().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(lines.fold(0) { acc: Long, line: String ->
            acc + getCorruptedLineScore(line)
        })
    }

    private fun getCorruptedLineScore(line: String): Long {
        val openers = ArrayList<Char>()

        line.forEach { char ->
            if (char == '(' || char == '[' || char == '{' || char == '<') {
                openers.add(char)
            } else {
                // Trying to close
                val lastOpener = openers.last()

                if (char != getInverted(lastOpener)) {
                    return when (char) {
                        ')' -> 3
                        ']' -> 57
                        '}' -> 1197
                        '>' -> 25137
                        else -> throw IllegalStateException("Impossibruh ! $char")
                    }
                } else {
                    openers.removeLast()
                }
            }
        }

        return 0
    }

    private fun getInverted(char: Char): Char = when (char) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalStateException("I don't know how to invert this char : $char")
    }

    private fun partTwo(lines: List<String>) {
        val incompleteLineScores = lines.mapNotNull { line ->
            getIncompleteLineScore(line).takeIf { it != 0L }
        }.sorted()

        println(incompleteLineScores[incompleteLineScores.size / 2])
    }

    private fun getIncompleteLineScore(line: String): Long {
        val openers = ArrayList<Char>()

        if (getCorruptedLineScore(line) != 0L) {
            return 0
        }

        line.forEach { char ->
            if (char == '(' || char == '[' || char == '{' || char == '<') {
                openers.add(char)
            } else {
                // Yolo
                openers.removeLast()
            }
        }

        return openers.foldRight(0) { char: Char, acc: Long ->
            acc * 5 + when (char) {
                '(' -> 1
                '[' -> 2
                '{' -> 3
                '<' -> 4
                else -> throw IllegalStateException("Impossibruh ! $char")
            }
        }
    }
}