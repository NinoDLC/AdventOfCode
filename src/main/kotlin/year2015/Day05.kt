package year2015

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
        println(
            lines.count { line ->
                line.count { char -> char in "aeiou" } >= 3
                    && line.zipWithNext().any { it.first == it.second }
                    && !line.contains("(ab|cd|pq|xy)".toRegex())
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.count { line -> isLineTheNewNice(line) }
        )
    }

    private fun isLineTheNewNice(line: String): Boolean {
        val pairsWithIndex = line.mapIndexedNotNull { index, char ->
            val next = line.getOrNull(index + 1)
            next?.let {
                PairWithIndex(
                    index = index,
                    first = char,
                    second = next
                )
            }
        }

        val firstRule = pairsWithIndex.any { a ->
            pairsWithIndex.any { b ->
                a.first == b.first && a.second == b.second && a.index != b.index && a.index + 1 < b.index
            }
        }

        val secondRule = pairsWithIndex.any { a ->
            pairsWithIndex.any { b ->
                a.first == b.second && a.index + 1 == b.index
            }
        }

        return firstRule && secondRule
    }

    data class PairWithIndex(
        val index: Int,
        val first: Char,
        val second: Char
    )
}