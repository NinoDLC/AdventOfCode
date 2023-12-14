package year2023

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime

class Day14 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day14().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day14().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val plane = Plane.of(lines)

        tiltNorth(plane)

        println(countNorthLoad(plane))
    }

    private fun partTwo(lines: List<String>) {
        val plane = Plane.of(lines)

        var i = 0
        val previousPlanesAppearances = mutableMapOf<Plane<Char>, Int>()

        while (true) {
            i++
            tiltNorth(plane)
            tiltWest(plane)
            tiltSouth(plane)
            tiltEast(plane)

            val currentPlane = plane.clone()
            val previousAppearance = previousPlanesAppearances[currentPlane]

            if (previousAppearance != null) {
                if ((1000000000 - previousAppearance) % (i - previousAppearance) == 0) {
                    break
                }
            } else {
                previousPlanesAppearances[currentPlane] = i
            }
        }

        println(countNorthLoad(plane))
    }

    private fun tiltNorth(plane: Plane<Char>) {
        plane.forEachColumn { column ->
            var nextSpotY = 0

            column.items.forEach { itemPosition ->
                if (itemPosition.item == 'O') {
                    if (nextSpotY != itemPosition.position.y) {
                        plane.transform(x = column.x, y = nextSpotY++) { 'O' }
                        plane.transform(x = column.x, y = itemPosition.position.y) { '.' }
                    } else if (nextSpotY == itemPosition.position.y) {
                        nextSpotY++
                    }
                } else if (itemPosition.item == '#') {
                    nextSpotY = itemPosition.position.y + 1
                }
            }
        }
    }

    private fun tiltWest(plane: Plane<Char>) {
        plane.forEachRow { row ->
            var nextSpotX = 0

            row.items.forEach { itemPosition ->
                if (itemPosition.item == 'O') {
                    if (nextSpotX != itemPosition.position.x) {
                        plane.transform(y = row.y, x = nextSpotX++) { 'O' }
                        plane.transform(y = row.y, x = itemPosition.position.x) { '.' }
                    } else if (nextSpotX == itemPosition.position.x) {
                        nextSpotX++
                    }
                } else if (itemPosition.item == '#') {
                    nextSpotX = itemPosition.position.x + 1
                }
            }
        }
    }

    private fun tiltSouth(plane: Plane<Char>) {
        plane.forEachColumn { column ->
            var nextSpotY = plane.yMax - 1

            column.items.reversed().forEach { itemPosition ->
                if (itemPosition.item == 'O') {
                    if (nextSpotY != itemPosition.position.y) {
                        plane.transform(x = column.x, y = nextSpotY--) { 'O' }
                        plane.transform(x = column.x, y = itemPosition.position.y) { '.' }
                    } else if (nextSpotY == itemPosition.position.y) {
                        nextSpotY--
                    }
                } else if (itemPosition.item == '#') {
                    nextSpotY = itemPosition.position.y - 1
                }
            }
        }
    }

    private fun tiltEast(plane: Plane<Char>) {
        plane.forEachRow { row ->
            var nextSpotX = plane.xMax - 1

            row.items.reversed().forEach { itemPosition ->
                if (itemPosition.item == 'O') {
                    if (nextSpotX != itemPosition.position.x) {
                        plane.transform(y = row.y, x = nextSpotX--) { 'O' }
                        plane.transform(y = row.y, x = itemPosition.position.x) { '.' }
                    } else if (nextSpotX == itemPosition.position.x) {
                        nextSpotX--
                    }
                } else if (itemPosition.item == '#') {
                    nextSpotX = itemPosition.position.x - 1
                }
            }
        }
    }

    private fun countNorthLoad(plane: Plane<Char>) = plane.rows.sumOf { row ->
        row.items.count { it.item == 'O' } * (plane.yMax - row.y)
    }
}