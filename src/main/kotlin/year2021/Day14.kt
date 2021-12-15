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

        var currentPolymerPairs: MutableMap<Pair<Char, Char>, Long> = lines
            .first()
            .zipWithNext()
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()

        val polymerCount = mutableMapOf<Char, Long>()

        lines.first().forEach {
            polymerCount.merge(it, 1) { previous, new -> previous + new }
        }

        repeat(times) {
            val newPolymerPairs = mutableMapOf<Pair<Char, Char>, Long>()

            currentPolymerPairs.forEach { entry ->
                val newPolymer = getNewPolymer(entry.key.first, entry.key.second, polymerRules)
                val leftPair = Pair(entry.key.first, newPolymer)
                val rightPair = Pair(newPolymer, entry.key.second)

                polymerCount.merge(newPolymer, entry.value) { previous, new -> previous + new }
                newPolymerPairs[leftPair] = newPolymerPairs.getOrPut(leftPair) { 0 } + entry.value
                newPolymerPairs[rightPair] = newPolymerPairs.getOrPut(rightPair) { 0 } + entry.value
            }

            currentPolymerPairs = newPolymerPairs
        }

        polymerCount.entries.sortedByDescending { it.value }.let {
            println(it.first().value - it.last().value)
        }
    }

    private fun getNewPolymer(first: Char, second: Char, polymerRules: List<Triple<Char, Char, Char>>): Char = polymerRules.single {
        it.first == first && it.second == second
    }.third
}