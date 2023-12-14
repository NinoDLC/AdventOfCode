package year2015

import utils.Plane
import utils.Plane.Companion.sum
import utils.getPuzzleInput
import utils.logMeasureTime

class Day06 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day06().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day06().partTwo(lines)
            }
            println()
        }

        private val INSTRUCTION_REGEX = """
            (toggle|turn on|turn off) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)
        """.trimIndent().toRegex()
    }

    private fun partOne(lines: List<String>) {
        val plane = Plane.of(1_000, 1_000) { _, _ -> false }

        lines.forEach { line ->
            val (operation, minX, minY, maxX, maxY) = INSTRUCTION_REGEX.find(line)!!.destructured
            plane.transform(
                xProgression = minX.toInt()..maxX.toInt(),
                yProgression = minY.toInt()..maxY.toInt(),
            ) { (existing: Boolean) ->
                when (operation) {
                    "toggle" -> !existing
                    "turn on" -> true
                    "turn off" -> false
                    else -> throw IllegalStateException("Unknown operation: $operation")
                }
            }
        }

        println(
            plane.sumOf { (turnedOn) ->
                if (turnedOn) {
                    1
                } else {
                    0
                }
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val plane = Plane.of(1_000, 1_000) { _, _ -> 0 }

        lines.forEach { line ->
            val (operation, minX, minY, maxX, maxY) = INSTRUCTION_REGEX.find(line)!!.destructured
            plane.transform(
                xProgression = minX.toInt()..maxX.toInt(),
                yProgression = minY.toInt()..maxY.toInt(),
            ) { (lightValue: Int) ->
                when (operation) {
                    "toggle" -> lightValue + 2
                    "turn on" -> lightValue + 1
                    "turn off" -> (lightValue - 1).coerceAtLeast(0)
                    else -> throw IllegalStateException("Unknown operation: $operation")
                }
            }
        }

        println(plane.sum())
    }
}