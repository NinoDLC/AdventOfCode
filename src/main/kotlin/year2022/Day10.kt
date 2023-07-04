package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day10 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
        println(
            (20..220 step 40).fold(0) { acc: Int, i: Int ->
                acc + (getRegisterValue(lines, i) * i)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            (0..5).fold("") { screenLines: String, y: Int ->
                val newLine = (0..39).fold("") { acc: String, x: Int ->
                    val cycle = x + 1
                    val registerValue = getRegisterValue(lines, cycle + y * 40)

                    acc.plus(
                        if (registerValue == x || registerValue == x - 1 || registerValue == x + 1) {
                            "#"
                        } else {
                            " "
                        }
                    )
                }

                screenLines + "\n" + newLine
            }
        )
    }

    private fun getRegisterValue(lines: List<String>, cycle: Int): Int {
        var currentCycle = 0
        var currentCount = 1

        lines.forEach { line ->
            if (line == "noop") {
                currentCycle++
            } else {
                currentCycle += 2

                if (currentCycle < cycle) {
                    currentCount += line.split(" ").last().toInt()
                } else {
                    return currentCount
                }
            }

            if (currentCycle >= cycle) {
                return currentCount
            }
        }

        return currentCount
    }
}