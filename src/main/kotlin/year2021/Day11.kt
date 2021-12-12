package year2021

import utils.logMeasureTime
import java.io.File

class Day11 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day11.txt").readLines()

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
        val map = ArrayList<Position>()

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                map.add(
                    Position(
                        x,
                        y,
                        char.toString().toInt(),
                        false
                    )
                )
            }
        }

        //print2DPositions(map.map { Triple(it.x, it.y, it.flashLevel) })

        var flashes = 0

        for (repeat in 1..100) {
            // Step 1
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    position.flashLevel++
                }
            }

            // Step 2
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    if (position.flashLevel >= 10) {
                        flashes += flash(map, position)
                    }
                }
            }

            // Step 3
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    position.flashed = false
                }
            }
        }

        println(flashes)
    }

    private fun partTwo(lines: List<String>) {
        val map = ArrayList<Position>()

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                map.add(
                    Position(
                        x,
                        y,
                        char.toString().toInt(),
                        false
                    )
                )
            }
        }

        var repeat = 0
        var allFlashed: Boolean
        do {
            repeat++
            allFlashed = true

            // Step 1
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    position.flashLevel++
                }
            }

            // Step 2
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    if (position.flashLevel >= 10) {
                        flash(map, position)
                    }
                }
            }

            // Step 3
            for (y in 0..9) {
                for (x in 0..9) {
                    val position = map.first { it.x == x && it.y == y }

                    if (!position.flashed) {
                        allFlashed = false
                    }
                    position.flashed = false
                }
            }
        } while (!allFlashed)

        println(repeat)
    }

    private fun flash(
        map: List<Position>,
        position: Position
    ): Int {
        var flashes = 1

        position.flashLevel = 0
        position.flashed = true
        if (position.x > 0) {
            val left = map.first { it.x == position.x - 1 && it.y == position.y }
            if (!left.flashed) {
                left.flashLevel++

                if (left.flashLevel >= 10) {
                    flashes += flash(map, left)
                }
            }
            if (position.y > 0) {
                val topLeft = map.first { it.x == position.x - 1 && it.y == position.y - 1 }
                if (!topLeft.flashed) {
                    topLeft.flashLevel++

                    if (topLeft.flashLevel >= 10) {
                        flashes += flash(map, topLeft)
                    }
                }
            }
            if (position.y < 9) {
                val bottomLeft = map.first { it.x == position.x - 1 && it.y == position.y + 1 }
                if (!bottomLeft.flashed) {
                    bottomLeft.flashLevel++

                    if (bottomLeft.flashLevel >= 10) {
                        flashes += flash(map, bottomLeft)
                    }
                }
            }
        }
        if (position.y > 0) {
            val top = map.first { it.x == position.x && it.y == position.y - 1 }
            if (!top.flashed) {
                top.flashLevel++

                if (top.flashLevel >= 10) {
                    flashes += flash(map, top)
                }
            }
        }
        if (position.x < 9) {
            val right = map.first { it.x == position.x + 1 && it.y == position.y }
            if (!right.flashed) {
                right.flashLevel++

                if (right.flashLevel >= 10) {
                    flashes += flash(map, right)
                }
            }
            if (position.y > 0) {
                val topRight = map.first { it.x == position.x + 1 && it.y == position.y - 1 }
                if (!topRight.flashed) {
                    topRight.flashLevel++

                    if (topRight.flashLevel >= 10) {
                        flashes += flash(map, topRight)
                    }
                }
            }
            if (position.y < 9) {
                val bottomRight = map.first { it.x == position.x + 1 && it.y == position.y + 1 }
                if (!bottomRight.flashed) {
                    bottomRight.flashLevel++

                    if (bottomRight.flashLevel >= 10) {
                        flashes += flash(map, bottomRight)
                    }
                }
            }
        }
        if (position.y < 9) {
            val bottom = map.first { it.x == position.x && it.y == position.y + 1 }
            if (!bottom.flashed) {
                bottom.flashLevel++

                if (bottom.flashLevel >= 10) {
                    flashes += flash(map, bottom)
                }
            }
        }

        return flashes
    }

    private class Position(
        val x: Int,
        val y: Int,
        var flashLevel: Int,
        var flashed: Boolean
    )
}