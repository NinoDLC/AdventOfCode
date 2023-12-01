package year2016

import utils.logMeasureTime
import java.io.File
import kotlin.math.abs

class Day01 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2016/day01.txt").readLines()

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
        val instructions: List<Instruction> = getInstructions(lines)

        val position: Position = instructions.fold(Position(0, 0, Orientation.NORTH)) { previousPosition, instruction ->
            val newOrientation = getNewOrientation(instruction, previousPosition)

            Position(
                x = newOrientation.xTransformation(previousPosition.x, instruction.blockCount),
                y = newOrientation.yTransformation(previousPosition.y, instruction.blockCount),
                orientation = newOrientation
            )
        }

        println(abs(position.x) + abs(position.y))
    }

    private fun partTwo(lines: List<String>) {
        val instructions: List<Instruction> = getInstructions(lines)
        val previousPositions = mutableListOf(Position(0, 0, Orientation.NORTH))

        for (instruction in instructions) {
            val previousPosition = previousPositions.last()
            val newOrientation = getNewOrientation(instruction, previousPosition)

            var walkedBlocks = 0
            while (walkedBlocks++ < instruction.blockCount) {
                when (newOrientation) {
                    Orientation.NORTH,
                    Orientation.SOUTH -> previousPositions.add(
                        Position(
                            x = newOrientation.xTransformation(previousPosition.x, instruction.blockCount),
                            y = newOrientation.yTransformation(previousPosition.y, walkedBlocks),
                            orientation = newOrientation
                        )
                    )
                    Orientation.EAST,
                    Orientation.WEST -> previousPositions.add(
                        Position(
                            x = newOrientation.xTransformation(previousPosition.x, walkedBlocks),
                            y = newOrientation.yTransformation(previousPosition.y, instruction.blockCount),
                            orientation = newOrientation
                        )
                    )
                }
            }

            val duplicatedPosition = getDuplicatedPosition(previousPositions)

            if (duplicatedPosition != null) {
                println(abs(duplicatedPosition.x) + abs(duplicatedPosition.y))
                break
            }
        }
    }

    private fun getDuplicatedPosition(positions: MutableList<Position>): Position? {
        val addedPositions = mutableSetOf<Pair<Int, Int>>()

        positions.forEach { position ->
            if (!addedPositions.add(position.x to position.y)) {
                return position
            }
        }

        return null
    }

    private fun getInstructions(lines: List<String>): List<Instruction> {
        val instructions: List<Instruction> = lines.first().split(", ").map { instruction ->
            Instruction(
                turnDirection = if (instruction.first() == 'L') {
                    TurnDirection.LEFT
                } else {
                    TurnDirection.RIGHT
                },
                blockCount = instruction.drop(1).toInt()
            )
        }
        return instructions
    }

    private fun getNewOrientation(
        instruction: Instruction,
        previousPosition: Position
    ) = Orientation.values()[
        when (instruction.turnDirection) {
            TurnDirection.LEFT -> (previousPosition.orientation.ordinal + (Orientation.values().count() - 1)) % Orientation.values().count()
            TurnDirection.RIGHT -> (previousPosition.orientation.ordinal + 1) % Orientation.values().count()
        }
    ]

    private data class Instruction(
        val turnDirection: TurnDirection,
        val blockCount: Int,
    )

    private enum class TurnDirection {
        LEFT,
        RIGHT,
    }

    private data class Position(
        val x: Int,
        val y: Int,
        val orientation: Orientation,
    )

    private enum class Orientation(
        val xTransformation: (x: Int, blockCount: Int) -> Int,
        val yTransformation: (y: Int, blockCount: Int) -> Int,
    ) {
        NORTH(
            xTransformation = { x, _ -> x },
            yTransformation = { y, blockCount -> y - blockCount },
        ),
        EAST(
            xTransformation = { x, blockCount -> x + blockCount },
            yTransformation = { y, _ -> y },
        ),
        SOUTH(
            xTransformation = { x, _ -> x },
            yTransformation = { y, blockCount -> y + blockCount },
        ),
        WEST(
            xTransformation = { x, blockCount -> x - blockCount },
            yTransformation = { y, _ -> y },
        ),
    }
}