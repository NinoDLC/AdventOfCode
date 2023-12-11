package year2023

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime
import year2023.Day10.EdgeSide.*
import year2023.Day10.Tile.*

class Day10 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this).map { line ->
                line.map { char ->
                    Tile.entries.find { it.weirdInputChar == char }
                        ?: throw IllegalStateException("Unknown char $char in line $line")
                }
            }

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

    private fun partOne(lines: List<List<Tile>>) {
        val plane = Plane.of(lines)
        val startTile = plane.positionOf { (item) -> item == START }

        val nextTile = plane.upOf(startTile)?.takeIf {
            it.item == VERTICAL || it.item == SOUTH_WEST || it.item == SOUTH_EAST
        } ?: plane.rightOf(startTile)?.takeIf {
            it.item == HORIZONTAL || it.item == SOUTH_WEST || it.item == NORTH_WEST
        } ?: plane.downOf(startTile)?.takeIf {
            it.item == VERTICAL || it.item == NORTH_EAST || it.item == NORTH_WEST
        } ?: plane.leftOf(startTile)?.takeIf {
            it.item == HORIZONTAL || it.item == NORTH_EAST || it.item == SOUTH_EAST
        } ?: throw IllegalStateException("Couldn't find a point connected to starting point")

        println(crawlUntilStart(plane, mutableListOf(), startTile, nextTile, 0) / 2)
    }

    private fun partTwo(lines: List<List<Tile>>) {
        val plane = Plane.of(lines)
        val startTile = plane.positionOf { (item) -> item == START }
        val nextTile = plane.upOf(startTile)?.takeIf {
            it.item == VERTICAL || it.item == SOUTH_WEST || it.item == SOUTH_EAST
        } ?: plane.rightOf(startTile)?.takeIf {
            it.item == HORIZONTAL || it.item == SOUTH_WEST || it.item == NORTH_WEST
        } ?: plane.downOf(startTile)?.takeIf {
            it.item == VERTICAL || it.item == NORTH_EAST || it.item == NORTH_WEST
        } ?: plane.leftOf(startTile)?.takeIf {
            it.item == HORIZONTAL || it.item == NORTH_EAST || it.item == SOUTH_EAST
        } ?: throw IllegalStateException("Couldn't find a point connected to starting point")
        val crawledTiles = mutableListOf<Plane.ItemPosition<Tile>>()
        crawledTiles.add(startTile)

        // Construct path
        crawlUntilStart(plane, crawledTiles, startTile, nextTile, 0)

        // Remove unrelated junk pipes
        plane.transform { itemPosition ->
            if (itemPosition !in crawledTiles) {
                GROUND
            } else {
                itemPosition.item
            }
        }

        val tilesInside = plane.fold(initialValue = 0L) { acc, itemPosition ->
            if (itemPosition !in crawledTiles) {
                // If path crossed is pair, it means we are outside the path
                acc + (countPathCrossedUntilEdge(plane, itemPosition) % 2)
            } else {
                acc
            }
        }

        println(tilesInside)
    }

    private tailrec fun crawlUntilStart(
        plane: Plane<Tile>,
        crawledTiles: MutableList<Plane.ItemPosition<Tile>>,
        previousTile: Plane.ItemPosition<Tile>,
        currentTile: Plane.ItemPosition<Tile>,
        crawledDistance: Long,
    ): Long {
        crawledTiles.add(currentTile)

        val nextTile = plane.get(
            when (currentTile.item) {
                HORIZONTAL -> if (currentTile.position.isToTheRightOf(previousTile.position)) {
                    currentTile.position.toRight()
                } else {
                    currentTile.position.toLeft()
                }

                VERTICAL -> if (currentTile.position.isToTheUpOf(previousTile.position)) {
                    currentTile.position.toUp()
                } else {
                    currentTile.position.toDown()
                }

                NORTH_EAST -> if (currentTile.position.isToTheLeftOf(previousTile.position)) {
                    currentTile.position.toUp()
                } else {
                    currentTile.position.toRight()
                }

                NORTH_WEST -> if (currentTile.position.isToTheRightOf(previousTile.position)) {
                    currentTile.position.toUp()
                } else {
                    currentTile.position.toLeft()
                }

                SOUTH_WEST -> if (currentTile.position.isToTheRightOf(previousTile.position)) {
                    currentTile.position.toDown()
                } else {
                    currentTile.position.toLeft()
                }

                SOUTH_EAST -> if (currentTile.position.isToTheLeftOf(previousTile.position)) {
                    currentTile.position.toDown()
                } else {
                    currentTile.position.toRight()
                }

                GROUND -> throw IllegalStateException(
                    "We're in the dirt captain... previousTile = $previousTile, currentTile = $currentTile, crawledDistance = $crawledDistance"
                )

                START -> return crawledDistance + 1
            }
        )

        return crawlUntilStart(plane, crawledTiles, currentTile, nextTile, crawledDistance + 1)
    }

    // We try to go up and count the path we encounter, expect if we cross "START" that is unknown tile for us
    // In this case, we simply go down instead
    private fun countPathCrossedUntilEdge(plane: Plane<Tile>, itemPosition: Plane.ItemPosition<Tile>): Long =
        if (plane.none(
                xProgression = itemPosition.position.x..itemPosition.position.x,
                yProgression = (itemPosition.position.y - 1).coerceAtLeast(0) downTo 0,
            ) { it.item == START }
        ) {
            // Go up and count paths crossed
            var edgeSide = NONE
            plane.sumOf(
                itemPosition.position.x..itemPosition.position.x,
                (itemPosition.position.y - 1).coerceAtLeast(0) downTo 0,
            ) { itemPositionToUpEdge ->
                when (itemPositionToUpEdge.item) {
                    HORIZONTAL -> 1
                    VERTICAL -> 0
                    NORTH_EAST -> {
                        edgeSide = LEFT
                        0
                    }

                    NORTH_WEST -> {
                        edgeSide = RIGHT
                        0
                    }

                    SOUTH_WEST -> when (edgeSide) {
                        NONE -> throw IllegalStateException("NONE edgeSide while going up for tile $itemPositionToUpEdge")
                        LEFT -> 1
                        RIGHT -> 0
                    }

                    SOUTH_EAST -> when (edgeSide) {
                        NONE -> throw IllegalStateException("NONE edgeSide while going up for tile $itemPositionToUpEdge")
                        LEFT -> 0
                        RIGHT -> 1
                    }

                    GROUND -> 0
                    START -> throw IllegalStateException("START encountered, I'm lost!")
                }
            }
        } else {
            // Go down and count paths crossed
            var edgeSide = NONE
            plane.sumOf(
                itemPosition.position.x..itemPosition.position.x,
                (itemPosition.position.y + 1).coerceAtMost(plane.yMax) until plane.yMax,
            ) { itemPositionToUpEdge ->
                when (itemPositionToUpEdge.item) {
                    HORIZONTAL -> 1
                    VERTICAL -> 0
                    SOUTH_EAST -> {
                        edgeSide = LEFT
                        0
                    }

                    SOUTH_WEST -> {
                        edgeSide = RIGHT
                        0
                    }

                    NORTH_WEST -> when (edgeSide) {
                        NONE -> throw IllegalStateException("NONE edgeSide while going down for tile $itemPositionToUpEdge")
                        LEFT -> 1
                        RIGHT -> 0
                    }

                    NORTH_EAST -> when (edgeSide) {
                        NONE -> throw IllegalStateException("NONE edgeSide while going down for tile $itemPositionToUpEdge")
                        LEFT -> 0
                        RIGHT -> 1
                    }

                    GROUND -> 0
                    START -> throw IllegalStateException("START encountered, I'm lost!")
                }
            }
        }

    private enum class Tile(val weirdInputChar: Char, val representation: String) {
        HORIZONTAL('-', "─"),
        VERTICAL('|', "│"),
        NORTH_EAST('L', "└"),
        NORTH_WEST('J', "┘"),
        SOUTH_WEST('7', "┐"),
        SOUTH_EAST('F', "┌"),
        GROUND('.', "▪"),
        START('S', "S");

        override fun toString(): String = representation
    }

    private enum class EdgeSide {
        NONE,
        LEFT,
        RIGHT,
    }
}