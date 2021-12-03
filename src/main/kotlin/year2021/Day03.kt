package year2021

import utils.logMeasureTime
import java.io.File

class Day03 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day03.txt").readLines()

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
        val list: Array<Int> = Array(12) { 0 }

        lines.forEach { line ->
            line.forEachIndexed { index, char ->
                list[index] = list[index] + if (char == '1') {
                    1
                } else {
                    -1
                }
            }
        }

        val gammaBinary = list.joinToString(separator = "") { if (it > 0) "1" else "0" }
        val gammaValue = Integer.parseInt(gammaBinary, 2)
        val epsilonBinary = list.joinToString(separator = "") { if (it < 0) "1" else "0" }
        val epsilonValue = Integer.parseInt(epsilonBinary, 2)

        println(gammaValue * epsilonValue)
    }

    private fun partTwo(lines: List<String>) {
        val oxygenValue = computeRatio(lines) {
            if (it >= 0) {
                '1'
            } else {
                '0'
            }
        }
        val co2Value = computeRatio(lines) {
            if (it >= 0) {
                '0'
            } else {
                '1'
            }
        }

        println(oxygenValue * co2Value)
    }

    private fun computeRatio(lines: List<String>, onRatioComputed: (Int) -> Char): Int {
        val binaryStringBuilder = StringBuilder()

        for (i in 0..11) {
            val filteredLines = lines.filter { it.startsWith(binaryStringBuilder) }

            if (filteredLines.size == 1) {
                binaryStringBuilder.clear()
                binaryStringBuilder.append(filteredLines.first())
                break
            }

            var ratio = 0

            filteredLines.forEach {
                ratio += if (it[i] == '1') {
                    1
                } else {
                    -1
                }
            }

            binaryStringBuilder.append(
                onRatioComputed(ratio)
            )
        }

        return Integer.parseInt(binaryStringBuilder.toString(), 2)
    }
}