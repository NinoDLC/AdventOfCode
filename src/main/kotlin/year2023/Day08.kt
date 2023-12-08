package year2023

import utils.getPuzzleInput
import utils.lcm
import utils.logMeasureTime

class Day08 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day08().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day08().partTwo(lines)
            }
            println()
        }

        private val REGEX_INSTRUCTION = "(.{3}) = \\((.{3}), (.{3})\\)".toRegex()
    }

    private fun partOne(lines: List<String>) {
        val instructions = lines.first()
        val directions = parsePlaceToDirections(lines.drop(2))

        println(
            countStepsUntil(instructions, directions, Place("AAA")) { it.value == "ZZZ" }
        )
    }

    private fun partTwo(lines: List<String>) {
        val instructions = lines.first()
        val placeToDirections = parsePlaceToDirections(lines.drop(2))

        println(
            placeToDirections.keys
                .filter { it.value.endsWith("A") }
                .map { placeStart -> countStepsUntil(instructions, placeToDirections, placeStart) { it.value.endsWith("Z") } }
                .reduce { previous, next -> previous.lcm(next) }
        )
    }

    private fun parsePlaceToDirections(lines: List<String>): Map<Place, Direction> = lines.associate { line ->
        val (startPoint, left, right) = REGEX_INSTRUCTION.find(line)!!.destructured

        Place(startPoint) to Direction(
            left = Place(left),
            right = Place(right),
        )
    }

    private fun countStepsUntil(
        instructions: String,
        directions: Map<Place, Direction>,
        placeStart: Place,
        predicate: (Place) -> Boolean,
    ): Long {
        var stepCount = 0L
        var currentPlace = placeStart

        while (true) {
            val nextInstruction = instructions[(stepCount++ % instructions.length.toLong()).toInt()]
            currentPlace = if (nextInstruction == 'L') {
                directions[currentPlace]!!.left
            } else {
                directions[currentPlace]!!.right
            }

            if (predicate(currentPlace)) {
                return stepCount
            }
        }
    }

    private data class Direction(
        val left: Place,
        val right: Place,
    )

    @JvmInline
    private value class Place(val value: String)
}