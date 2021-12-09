package year2015

import utils.logMeasureTime
import java.io.File

class Day02 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2015/day02.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day02().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day02().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.fold(0) { acc: Int, dimensions: String ->
                acc + getSurfaceForGift(dimensions)
            }
        )
    }

    private fun getSurfaceForGift(dimensions: String): Int {
        val parsedDimensions = dimensions.split("x").map { it.toInt() }.sorted()
        val surfaces = listOf(
            parsedDimensions[0] * parsedDimensions[1],
            parsedDimensions[1] * parsedDimensions[2],
            parsedDimensions[2] * parsedDimensions[0],
        ).sorted()

        return surfaces[0] * 3 + surfaces[1] * 2 + surfaces[2] * 2
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.fold(0) { acc: Int, dimensions: String ->
                acc + getRibbonForGift(dimensions)
            }
        )
    }

    private fun getRibbonForGift(dimensions: String): Int {
        val parsedDimensions = dimensions.split("x").map { it.toInt() }.sorted()
        val perimeters = listOf(
            parsedDimensions[0] * 2 + parsedDimensions[1] * 2,
            parsedDimensions[1] * 2 + parsedDimensions[2] * 2,
            parsedDimensions[2] * 2 + parsedDimensions[0] * 2,
        ).sorted()

        return perimeters[0] + parsedDimensions[0] * parsedDimensions[1] * parsedDimensions[2]
    }
}