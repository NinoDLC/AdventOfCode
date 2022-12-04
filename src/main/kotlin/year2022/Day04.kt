package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day04 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day04().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day04().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.count { line ->
                val firstTaskSections = line.substringBefore(",").let { task ->
                    task.substringBefore("-").toInt() to task.substringAfter("-").toInt()
                }
                val secondTaskSections = line.substringAfter(",").let { task ->
                    task.substringBefore("-").toInt() to task.substringAfter("-").toInt()
                }

                firstTaskSections.first <= secondTaskSections.first && firstTaskSections.second >= secondTaskSections.second
                    || secondTaskSections.first <= firstTaskSections.first && secondTaskSections.second >= firstTaskSections.second
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.count { line ->
                val firstTaskSections = line.substringBefore(",").let { task ->
                    task.substringBefore("-").toInt() to task.substringAfter("-").toInt()
                }
                val secondTaskSections = line.substringAfter(",").let { task ->
                    task.substringBefore("-").toInt() to task.substringAfter("-").toInt()
                }

                firstTaskSections.first <= secondTaskSections.first && firstTaskSections.second >= secondTaskSections.second
                    || secondTaskSections.first <= firstTaskSections.first && secondTaskSections.second >= firstTaskSections.second
                    || firstTaskSections.first <= secondTaskSections.first && firstTaskSections.second >= secondTaskSections.first
                    || secondTaskSections.first <= firstTaskSections.first && secondTaskSections.second >= firstTaskSections.first
            }
        )
    }
}