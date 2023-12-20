package year2023

import utils.*
import utils.Direction.*
import year2023.Day17.Axis.HORIZONTAL
import year2023.Day17.Axis.VERTICAL
import java.util.*

class Day17 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day17().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day17().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val heatLosses = Plane.of(lines).map { it.item.digitToInt() }

        println(getLowestHeatLossPath(heatLosses, 1, 3))
    }

    private fun partTwo(lines: List<String>) {
        val heatLosses = Plane.of(lines).map { it.item.digitToInt() }

        println(getLowestHeatLossPath(heatLosses, 4, 10))
    }

    private fun getLowestHeatLossPath(heatLosses: Plane<Int>, minMoves: Int, maxMoves: Int): Int {
        val lowestHeatLossesPerTile: Plane<MutableMap<Direction, Int>> = heatLosses.map { item ->
            mutableMapOf(
                NORTH to (item.position.x * 9) + (item.position.y * 9),
                EAST to (item.position.x * 9) + (item.position.y * 9),
                SOUTH to (item.position.x * 9) + (item.position.y * 9),
                WEST to (item.position.x * 9) + (item.position.y * 9),
            )
        }

        val pathQueue = PriorityQueue<Path>()
        pathQueue.add(
            Path(
                currentPosition = Position(0, 0),
                totalHeatLoss = 0,
                lastDirection = EAST,
            )
        )
        pathQueue.add(
            Path(
                currentPosition = Position(0, 0),
                totalHeatLoss = 0,
                lastDirection = SOUTH,
            )
        )

        while (pathQueue.isNotEmpty()) {
            val currentPath = pathQueue.poll()
            val nextAxis = when (currentPath.lastDirection) {
                NORTH,
                SOUTH -> HORIZONTAL

                EAST,
                WEST -> VERTICAL
            }

            getNextAvailablePositions(currentPath.currentPosition, nextAxis, minMoves, maxMoves).forEach { nextPosition ->
                if (
                    nextPosition.x >= 0 && nextPosition.x < heatLosses.xMax &&
                    nextPosition.y >= 0 && nextPosition.y < heatLosses.yMax
                ) {
                    val going = currentPath.currentPosition.getDirectionTo(nextPosition)
                    val blocks = currentPath.currentPosition.getDistanceTo(nextPosition)
                    val nextHeatLoss = (1..blocks).fold(currentPath.totalHeatLoss) { acc: Int, deltaTile: Int ->
                        acc + when (going) {
                            NORTH -> heatLosses[currentPath.currentPosition.toNorth(deltaTile)].item
                            EAST -> heatLosses[currentPath.currentPosition.toEast(deltaTile)].item
                            SOUTH -> heatLosses[currentPath.currentPosition.toSouth(deltaTile)].item
                            WEST -> heatLosses[currentPath.currentPosition.toWest(deltaTile)].item
                        }
                    }

                    val lowestHeatLossesOnNextTile = lowestHeatLossesPerTile[nextPosition].item
                    val lowestHeatLossOnNextTileForDirection = lowestHeatLossesOnNextTile[going]!!
                    if (nextHeatLoss < lowestHeatLossOnNextTileForDirection) {
                        lowestHeatLossesOnNextTile[going] = nextHeatLoss
                        pathQueue.add(
                            Path(
                                currentPosition = nextPosition,
                                totalHeatLoss = nextHeatLoss,
                                lastDirection = going,
                            )
                        )
                    }
                }
            }
        }

        return lowestHeatLossesPerTile[lowestHeatLossesPerTile.xMax - 1, lowestHeatLossesPerTile.yMax - 1].item.minOf { entry ->
            entry.value
        }
    }

    private fun getNextAvailablePositions(
        position: Position,
        axis: Axis,
        minMoves: Int,
        maxMoves: Int
    ): List<Position> = (minMoves..maxMoves).map {
        when (axis) {
            VERTICAL -> listOf(
                position.copy(y = position.y + it),
                position.copy(y = position.y - it),
            )

            HORIZONTAL -> listOf(
                position.copy(x = position.x + it),
                position.copy(x = position.x - it),
            )
        }
    }.flatten()

    private data class Path(
        val currentPosition: Position,
        val totalHeatLoss: Int,
        val lastDirection: Direction,
    ) : Comparable<Path> {
        override fun compareTo(other: Path): Int = totalHeatLoss - other.totalHeatLoss
    }

    private enum class Axis {
        VERTICAL,
        HORIZONTAL
    }
}