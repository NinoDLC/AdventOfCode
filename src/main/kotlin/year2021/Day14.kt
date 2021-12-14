package year2021

import utils.logMeasureTime
import java.io.File

class Day14 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day14.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day14().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day14().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        runPolymerisation(lines, 10)
    }

    private fun partTwo(lines: List<String>) {
        runPolymerisation(lines, 40)
    }

    private fun runPolymerisation(lines: List<String>, times: Int) {
        val polymerRules = lines.drop(2).map { Triple(it.first(), it[1], it.last()) }

        (0 until times).fold(lines.first().toList()) { acc: List<Char>, time ->
            println("$time: ${acc.size}")
            acc.mapIndexed { index, char ->
                val next = acc.getOrNull(index + 1)

                if (next == null) {
                    listOf(char)
                } else {
                    listOf(char, getNewPolymer(char, next, polymerRules))
                }
            }.flatten()
        }.groupingBy {
            it
        }.eachCount().entries.sortedByDescending {
            it.value
        }.let {
            println(it.first().value - it.last().value)
        }
    }

    private fun getNewPolymer(first: Char, second: Char, polymerRules: List<Triple<Char, Char, Char>>): Char = polymerRules.single {
        it.first == first && it.second == second
    }.third
}