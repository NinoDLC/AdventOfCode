package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day09 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day09().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day09().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val tailPositions = doTheSnake(
            commands = lines,
            knots = arrayOf(
                Position(0, 0),
                Position(0, 0)
            ),
            trackedKnotIndex = 1
        )

        println(tailPositions.size)
    }

    private fun partTwo(lines: List<String>) {
        val tailPositions = doTheSnake(
            commands = lines,
            knots = Array(10) { Position(0, 0) },
            trackedKnotIndex = 9
        )

        println(tailPositions.size)
    }

    private fun doTheSnake(commands: List<String>, knots: Array<Position>, trackedKnotIndex: Int): Set<Position> {
        val trackedKnotPositions = mutableSetOf<Position>()

        commands.forEach { command ->
            val direction = command.first()
            val repeat = command.split(" ").last().toInt()

            repeat(repeat) {
                knots.forEachIndexed { index, position ->
                    if (index == 0) {
                        when (direction) {
                            'U' -> knots[0] = position.copy(y = position.y + 1)
                            'R' -> knots[0] = position.copy(x = position.x + 1)
                            'D' -> knots[0] = position.copy(y = position.y - 1)
                            'L' -> knots[0] = position.copy(x = position.x - 1)
                        }
                    } else {
                        val newPositionForCurrentKnot = updatePosition(
                            frontKnot = knots[index - 1],
                            currentKnot = knots[index]
                        )

                        knots[index] = newPositionForCurrentKnot
                    }
                }

                trackedKnotPositions.add(knots[trackedKnotIndex])
            }
        }

        return trackedKnotPositions
    }

    private fun updatePosition(frontKnot: Position, currentKnot: Position): Position =
        if (currentKnot.x !in frontKnot.x - 1..frontKnot.x + 1
            || currentKnot.y !in frontKnot.y - 1..frontKnot.y + 1
        ) {
            Position(
                x = if ((frontKnot.x + currentKnot.x) % 2 == 0) {
                    (frontKnot.x + currentKnot.x) / 2
                } else {
                    frontKnot.x
                },
                y = if ((frontKnot.y + currentKnot.y) % 2 == 0) {
                    (frontKnot.y + currentKnot.y) / 2
                } else {
                    frontKnot.y
                }
            )
        } else {
            currentKnot
        }

    private data class Position(
        val x: Int,
        val y: Int,
    )
}