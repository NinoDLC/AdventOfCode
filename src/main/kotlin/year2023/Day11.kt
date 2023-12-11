package year2023

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime

class Day11 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day11().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day11().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val expandedUniverse = getExpandedUniverse(lines)
        val galaxies = expandedUniverse.plane.filter { it.item == '#' }

        println(
            sumPathsBetweenGalaxies(
                expandedUniverse = expandedUniverse,
                galaxies = galaxies,
                shiftPower = 2L,
            )
        )
    }

    private fun partTwo(lines: List<String>) {
        val expandedUniverse = getExpandedUniverse(lines)
        val galaxies = expandedUniverse.plane.filter { it.item == '#' }

        println(
            sumPathsBetweenGalaxies(
                expandedUniverse = expandedUniverse,
                galaxies = galaxies,
                shiftPower = 1_000_000L,
            )
        )
    }

    private fun getExpandedUniverse(lines: List<String>): ExpandedUniverse {
        val plane = Plane.of(lines)

        val shiftedRows = mutableListOf<Int>()
        plane.forEachRow { row ->
            if (row.items.all { it.item == '.' }) {
                shiftedRows.add(row.y)
            }
        }

        val shiftedColumns = mutableListOf<Int>()
        plane.forEachColumn { column ->
            if (column.items.all { it.item == '.' }) {
                shiftedColumns.add(column.x)
            }
        }

        return ExpandedUniverse(
            plane = plane,
            shiftedColumns = shiftedColumns,
            shiftedRows = shiftedRows,
        )
    }

    private fun sumPathsBetweenGalaxies(
        expandedUniverse: ExpandedUniverse,
        galaxies: List<Plane.ItemPosition<Char>>,
        shiftPower: Long,
    ): Long = galaxies.indices.sumOf { index ->
        val galaxy = galaxies[index]
        galaxies.drop(index + 1).sumOf { otherGalaxy ->
            val lowestXPos = galaxy.position.x.coerceAtMost(otherGalaxy.position.x)
            val highestXPos = galaxy.position.x.coerceAtLeast(otherGalaxy.position.x)
            val emptySpaceExpansionX = (lowestXPos..highestXPos).count { originalX ->
                originalX in expandedUniverse.shiftedColumns
            } * (shiftPower - 1)

            val lowestYPos = galaxy.position.y.coerceAtMost(otherGalaxy.position.y)
            val highestYPos = galaxy.position.y.coerceAtLeast(otherGalaxy.position.y)
            val emptySpaceExpansionY = (lowestYPos..highestYPos).count { originalY ->
                originalY in expandedUniverse.shiftedRows
            } * (shiftPower - 1)

            (highestXPos - lowestXPos) + (highestYPos - lowestYPos) + emptySpaceExpansionX + emptySpaceExpansionY
        }
    }

    private data class ExpandedUniverse(
        val plane: Plane<Char>,
        val shiftedColumns: List<Int>,
        val shiftedRows: List<Int>,
    )
}