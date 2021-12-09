package year2015

import utils.logMeasureTime
import java.io.File

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2015/day01.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day01().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day01().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.first().fold(0) { acc: Int, c: Char ->
                acc + if (c == '(') {
                    1
                } else {
                    -1
                }
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        lines.first().foldIndexed(0) { index: Int, acc: Int, c: Char ->
            if (acc == -1) {
                println(index)
                return
            }
            acc + if (c == '(') {
                1
            } else {
                -1
            }
        }
    }
}