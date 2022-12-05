package year2022

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
        val firstPart = lines.takeWhile {
            it.isNotEmpty()
        }
        val crates: List<MutableList<Char>> = getCrates(firstPart)

        val secondPart = lines.takeLastWhile {
            it.isNotEmpty()
        }

        val regex = Regex("move (\\d*) from (\\d*) to (\\d*)")
        secondPart.forEach { line ->
            val matchResults = regex.find(line)!!.groupValues

            // Groups start at 1, since 0 is the whole match...
            val crateCount = matchResults[1].toInt()
            // Starts at 1
            val from = matchResults[2].toInt() - 1
            // Starts at 1
            val to = matchResults[3].toInt() - 1

            repeat(crateCount) {
                crates[to].add(crates[from].removeLast())
            }
        }
        println(crates.map { it.last() }.joinToString(separator = ""))
    }

    private fun partTwo(lines: List<String>) {
        val firstPart = lines.takeWhile {
            it.isNotEmpty()
        }
        val crates: List<MutableList<Char>> = getCrates(firstPart)

        val secondPart = lines.takeLastWhile {
            it.isNotEmpty()
        }

        val regex = Regex("move (\\d*) from (\\d*) to (\\d*)")
        secondPart.forEach { line ->
            val matchResults = regex.find(line)!!.groupValues

            // Groups start at 1, since 0 is the whole match...
            val crateCount = matchResults[1].toInt()
            // Starts at 1
            val from = matchResults[2].toInt() - 1
            // Starts at 1
            val to = matchResults[3].toInt() - 1

            val moved = crates[from].takeLast(crateCount)
            repeat(crateCount) { crates[from].removeLast() }
            crates[to].addAll(moved)
        }
        println(crates.map { it.last() }.joinToString(separator = ""))
    }

    private fun getCrates(firstPart: List<String>): List<MutableList<Char>> {
        val indexes = firstPart.last()
        val crateCount = indexes.trim().split(" ").last { it.isNotBlank() }.toInt()
        val crates = firstPart.dropLast(1).reversed()

        return List(crateCount) { index ->
            // Starts at 1
            val stackIndex = index + 1
            val charIndexForStackIndex = indexes.indexOf("$stackIndex")

            crates.mapNotNull { line ->
                line[charIndexForStackIndex].takeIf { it.isLetter() }
            }.toMutableList()
        }
    }
}