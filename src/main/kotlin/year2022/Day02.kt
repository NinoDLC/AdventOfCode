package year2022

import utils.logMeasureTime
import java.io.File

class Day02 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2022/day02.txt").readLines()

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
            lines.sumOf { line ->
                val opponent = Play.fromOpponent(line.first())
                val you = Play.fromYou(line.last())

                getWinScore(opponent, you) + you.score
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.sumOf { line ->
                val opponent = Play.fromOpponent(line.first())
                val you = when (Outcome.from(line.last())) {
                    Outcome.LOSE -> when (opponent) {
                        Play.ROCK -> Play.SCISSORS
                        Play.PAPER -> Play.ROCK
                        Play.SCISSORS -> Play.PAPER
                    }

                    Outcome.DRAW -> opponent
                    Outcome.WIN -> when (opponent) {
                        Play.ROCK -> Play.PAPER
                        Play.PAPER -> Play.SCISSORS
                        Play.SCISSORS -> Play.ROCK
                    }
                }

                getWinScore(opponent, you) + you.score
            }
        )
    }

    private fun getWinScore(opponent: Play, you: Play): Int {
        val winScore = when (you) {
            Play.ROCK -> when (opponent) {
                Play.ROCK -> 3
                Play.PAPER -> 0
                Play.SCISSORS -> 6
            }

            Play.PAPER -> when (opponent) {
                Play.ROCK -> 6
                Play.PAPER -> 3
                Play.SCISSORS -> 0
            }

            Play.SCISSORS -> when (opponent) {
                Play.ROCK -> 0
                Play.PAPER -> 6
                Play.SCISSORS -> 3
            }
        }
        return winScore
    }

    enum class Play(val opponent: Char, val you: Char, val score: Int) {
        ROCK('A', 'X', 1),
        PAPER('B', 'Y', 2),
        SCISSORS('C', 'Z', 3);

        companion object {
            fun fromOpponent(char: Char): Play = Play.values().find { it.opponent == char }
                ?: throw IllegalStateException("Unknown play: $char")

            fun fromYou(char: Char): Play = Play.values().find { it.you == char }
                ?: throw IllegalStateException("Unknown play: $char")
        }
    }

    enum class Outcome(val outcome: Char) {
        LOSE('X'),
        DRAW('Y'),
        WIN('Z');

        companion object {
            fun from(char: Char): Outcome = Outcome.values().find { it.outcome == char }
                ?: throw IllegalStateException("Unknown outcome: $char")
        }
    }
}