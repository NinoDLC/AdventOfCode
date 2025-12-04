package year2025

import utils.getPuzzleInput
import utils.logMeasureTime
import kotlin.math.pow

class Day03 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
    println(getBatteryJoltage(lines, 2))
}

private fun partTwo(lines: List<String>) {
    println(getBatteryJoltage(lines, 12))
}

private fun getBatteryJoltage(
    lines: List<String>,
    depth: Int,
): Long = lines.sumOf { line ->
    val availableBatteries = line.mapIndexed { index, char ->
        Battery(
            joltage = char.digitToInt(),
            index = index,
        )
    }

    var indexOfFirst = 0
    var indexOfLast = availableBatteries.size - (depth - 1)
    var batteryJoltages = ""

    repeat(depth) {
        val selectedBattery = availableBatteries
            .subList(indexOfFirst, indexOfLast)
            .maxBy { it.joltage }

        indexOfFirst = selectedBattery.index + 1 // sublist() is inclusive for the first
        indexOfLast++

        batteryJoltages += selectedBattery.joltage
    }

    batteryJoltages.toLong()
}

private data class Battery(
    val joltage: Int,
    val index: Int,
)
}
