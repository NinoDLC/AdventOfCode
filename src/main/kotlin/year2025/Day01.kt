package year2025

import utils.getPuzzleInput
import utils.logMeasureTime

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
            lines.fold(
                RotationAndZeroHits(
                    rotationValue = 50,
                    zeroHitsCount = 0,
                )
            ) { acc, line ->
                val rotationValue = line.drop(1).toInt()
                val rotationValueWithSign = if (line.first() == 'L') {
                    -rotationValue
                } else {
                    rotationValue
                }

                // -50 % 100 = -50 instead of mathematically correct 50 so we need this trick
                val newRotationValue = ((acc.rotationValue + rotationValueWithSign % 100) + 100) % 100

                RotationAndZeroHits(
                    rotationValue = newRotationValue,
                    zeroHitsCount = acc.zeroHitsCount + if (newRotationValue == 0) {
                        1
                    } else {
                        0
                    }
                )
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.fold(
                RotationAndZeroHits(
                    rotationValue = 50,
                    zeroHitsCount = 0,
                )
            ) { acc, line ->
                val rotationValue = line.drop(1).toInt()
                val shouldAdd = line.first() == 'R'

                var newRotationValue = acc.rotationValue
                var zeroHitsCount = 0

                repeat(rotationValue) {
                    if (shouldAdd) {
                        newRotationValue += 1
                    } else {
                        newRotationValue -= 1
                    }

                    if (newRotationValue < 0) {
                        newRotationValue += 100
                    }

                    if (newRotationValue == 100) {
                        newRotationValue = 0
                    }

                    if (newRotationValue == 0) {
                        zeroHitsCount++
                    }
                }

                RotationAndZeroHits(
                    rotationValue = newRotationValue,
                    zeroHitsCount = acc.zeroHitsCount + zeroHitsCount
                )
            }
        )
    }

    data class RotationAndZeroHits(
        val rotationValue: Int,
        val zeroHitsCount: Int,
    )
}
