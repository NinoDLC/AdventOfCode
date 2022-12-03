package year2022

import utils.logMeasureTime
import java.io.File

class Day03 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2022/day03.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day03().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day03().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.map { line ->
                line.substring(0, line.length / 2) to line.substring(line.length / 2)
            }.map { pair ->
                pair.first.toCharArray().intersect(pair.second.toList().toSet()).first()
            }.sumOf { char ->
                getCharValue(char)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.fold(listOf(mutableListOf())) { acc: List<MutableList<String>>, line ->
                val list = acc.last()

                if (list.size < 3) {
                    list.also {
                        it.add(line)
                    }
                    acc
                } else {
                    acc.plus<MutableList<String>>(mutableListOf(line))
                }
            }.map { threeElves ->
                val uniques = threeElves.first().toCharArray().toMutableSet()
                uniques.retainAll(
                    (threeElves[1].toSet())
                )
                uniques.retainAll(
                    (threeElves[2].toSet())
                )
                uniques.first()
            }.sumOf { char ->
                getCharValue(char)
            }
        )
    }

    private fun getCharValue(char: Char) = if (char.isLowerCase()) {
        char - 'a' + 1
    } else {
        char - 'A' + 27
    }
}