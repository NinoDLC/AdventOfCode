package year2021

import utils.Plane
import utils.Position
import utils.getPuzzleInput
import utils.logMeasureTime

class Day15 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day15().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day15().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val chitonCaveRiskLevels: Plane<Int> = getChitonCaveRiskLevels(lines)

        println(getLowestRiskLevelPath(chitonCaveRiskLevels))
    }

    private fun partTwo(lines: List<String>) {
        val initialChitonCaveRiskLevels: Plane<Int> = getChitonCaveRiskLevels(lines)
        val updatedChitonCaveRiskLevels: Plane<Int> = Plane.of(
            xMax = initialChitonCaveRiskLevels.xMax * 5,
            yMax = initialChitonCaveRiskLevels.yMax * 5,
        ) { x, y ->
            val xOffset = x / initialChitonCaveRiskLevels.xMax
            val yOffset = y / initialChitonCaveRiskLevels.yMax

            val riskLevel = initialChitonCaveRiskLevels[x % initialChitonCaveRiskLevels.xMax, y % initialChitonCaveRiskLevels.yMax].item

            ensureLowerThan10(riskLevel + xOffset + yOffset)
        }

        println(getLowestRiskLevelPath(updatedChitonCaveRiskLevels))
    }

    private fun getChitonCaveRiskLevels(lines: List<String>): Plane<Int> = Plane.of(lines).map { it.item.digitToInt() }

    private fun getLowestRiskLevelPath(chitonCaveRiskLevels: Plane<Int>): Long {
        val lowestRiskLevelPerTile: Plane<Long> = chitonCaveRiskLevels.map { Long.MAX_VALUE }

        val pathQueue = ArrayDeque<Path>()
        pathQueue.add(
            Path(
                Position(0, 0),
                0,
            )
        )

        while (pathQueue.isNotEmpty()) {
            val currentPath = pathQueue.removeFirst()

            if (currentPath.totalRiskLevel <= lowestRiskLevelPerTile[currentPath.currentPosition.x, currentPath.currentPosition.y].item) {
                currentPath.currentPosition.neighbours.forEach { nextPosition ->
                    if (
                        nextPosition.x >= 0 && nextPosition.x < chitonCaveRiskLevels.xMax &&
                        nextPosition.y >= 0 && nextPosition.y < chitonCaveRiskLevels.yMax
                    ) {
                        val lowestRiskLevelAtThisTile = lowestRiskLevelPerTile[nextPosition.x, nextPosition.y].item
                        val nextRiskLevel = currentPath.totalRiskLevel + chitonCaveRiskLevels[nextPosition].item

                        if (nextRiskLevel < lowestRiskLevelAtThisTile) {
                            lowestRiskLevelPerTile[nextPosition.x, nextPosition.y] = nextRiskLevel

                            pathQueue.add(
                                Path(
                                    nextPosition,
                                    nextRiskLevel
                                )
                            )
                        }
                    }
                }
            }
        }

        return lowestRiskLevelPerTile[lowestRiskLevelPerTile.xMax - 1, lowestRiskLevelPerTile.yMax - 1].item
    }

    private tailrec fun ensureLowerThan10(i: Int): Int = if (i < 10) {
        i
    } else {
        ensureLowerThan10(i / 10 + i % 10)
    }

    private data class Path(
        val currentPosition: Position,
        val totalRiskLevel: Long,
    )
}