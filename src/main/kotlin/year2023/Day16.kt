package year2023

import utils.Plane
import utils.Position
import utils.getPuzzleInput
import utils.logMeasureTime
import year2023.Day16.Direction.*

class Day16 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day16().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day16().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(getEnergizedTileCount(lines, LightBeam(Position(0, 0), RIGHTWARD)))
    }

    private fun partTwo(lines: List<String>) {
        val xMax = lines.maxOf { it.length }
        val yMax = lines.size

        println(
            (0 until xMax).maxOf { x ->
                (0 until yMax).maxOf { y ->
                    if (x == 0 || y == 0 || x == xMax - 1 || y == yMax - 1) {
                        getEnergizedTileCount(
                            lines = lines,
                            lightBeam = LightBeam(
                                position = Position(x, y),
                                direction = when {
                                    x == 0 -> RIGHTWARD
                                    y == 0 -> DOWNWARD
                                    x == xMax - 1 -> LEFTWARD
                                    y == yMax - 1 -> UPWARD
                                    else -> throw IllegalStateException("Impossible to find starting direction for x = $x and y = $y")
                                }
                            )
                        )
                    } else {
                        0
                    }
                }
            }
        )
    }

    private fun getEnergizedTileCount(lines: List<String>, lightBeam: LightBeam): Long {
        val plane = Plane.of(lines)
        val lightBeamsPlane = Plane.of<List<Direction>>(lines) { emptyList() }

        crawl(plane, lightBeamsPlane, lightBeam)

        return lightBeamsPlane.sumOf { itemPosition ->
            if (itemPosition.item.isNotEmpty()) {
                1
            } else {
                0
            }
        }
    }

    private fun crawl(plane: Plane<Char>, lightBeamsPlane: Plane<List<Direction>>, lightBeam: LightBeam) {
        if (lightBeam.position.x >= 0
            && lightBeam.position.y >= 0
            && lightBeam.position.x < plane.xMax
            && lightBeam.position.y < plane.yMax
        ) {
            val existingLightBeam = lightBeamsPlane[lightBeam.position]
            if (existingLightBeam.item.any { it == lightBeam.direction }) {
                return
            }

            lightBeamsPlane.transform(lightBeam.position.x, lightBeam.position.y) { it.item + lightBeam.direction }

            val tile = plane[lightBeam.position]
            when (tile.item) {
                '.' -> crawl(plane, lightBeamsPlane, lightBeam.prolong())

                '|' -> {
                    when (lightBeam.direction) {
                        UPWARD,
                        DOWNWARD -> crawl(plane, lightBeamsPlane, lightBeam.prolong())

                        LEFTWARD,
                        RIGHTWARD -> {
                            crawl(plane, lightBeamsPlane, lightBeam.redirect(UPWARD))
                            crawl(plane, lightBeamsPlane, lightBeam.redirect(DOWNWARD))
                        }
                    }
                }

                '-' -> {
                    when (lightBeam.direction) {
                        UPWARD,
                        DOWNWARD -> {
                            crawl(plane, lightBeamsPlane, lightBeam.redirect(LEFTWARD))
                            crawl(plane, lightBeamsPlane, lightBeam.redirect(RIGHTWARD))
                        }

                        LEFTWARD,
                        RIGHTWARD -> crawl(plane, lightBeamsPlane, lightBeam.prolong())
                    }
                }

                '/' -> {
                    when (lightBeam.direction) {
                        UPWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(RIGHTWARD))
                        LEFTWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(DOWNWARD))
                        DOWNWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(LEFTWARD))
                        RIGHTWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(UPWARD))
                    }
                }

                '\\' -> {
                    when (lightBeam.direction) {
                        UPWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(LEFTWARD))
                        LEFTWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(UPWARD))
                        DOWNWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(RIGHTWARD))
                        RIGHTWARD -> crawl(plane, lightBeamsPlane, lightBeam.redirect(DOWNWARD))
                    }
                }

                else -> throw IllegalStateException("Unknown char: ${tile.item}")
            }
        }
    }

    private data class LightBeam(
        val position: Position,
        val direction: Direction,
    ) {
        fun prolong(): LightBeam = copy(
            position = when (direction) {
                UPWARD -> position.toUp()
                LEFTWARD -> position.toLeft()
                DOWNWARD -> position.toDown()
                RIGHTWARD -> position.toRight()
            }
        )

        fun redirect(newDirection: Direction): LightBeam = copy(
            position = when (newDirection) {
                UPWARD -> position.toUp()
                LEFTWARD -> position.toLeft()
                DOWNWARD -> position.toDown()
                RIGHTWARD -> position.toRight()
            },
            direction = newDirection,
        )
    }

    private enum class Direction {
        UPWARD,
        LEFTWARD,
        DOWNWARD,
        RIGHTWARD,
    }
}