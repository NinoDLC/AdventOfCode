package year2025

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime

class Day07 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day07().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day07().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val plane = Plane.of(lines)

        plane.transform { (item, position) ->
            if (item == '.') {
                val northItem = plane.getOrNull(position.toNorth()).item
                if (northItem == '|' || northItem == 'S') {
                    '|'
                } else {
                    val eastItem = plane.getOrNull(position.toEast()).item
                    if (eastItem == '^' && plane.getOrNull(position.toNorthEast()).item == '|') {
                        '|'
                    } else {
                        val westItem = plane.getOrNull(position.toWest()).item
                        if (westItem == '^' && plane.getOrNull(position.toNorthWest()).item == '|') {
                            '|'
                        } else {
                            item
                        }
                    }
                }
            } else {
                item
            }
        }

        println(
            plane.count { (item, position) ->
                item == '^' && plane.getOrNull(position.toNorth()).item == '|'
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val plane: Plane<Position> = Plane.of(lines) {
            when (it.item) {
                '.' -> Position.Nothing
                '^' -> Position.Splitter
                'S' -> Position.Start
                else -> throw IllegalStateException("Unknown char: $it")
            }
        }

        plane.transform { (item, position) ->
            var currentState = item

            when (item) {
                is Position.Nothing -> {
                    when (val northItem = plane.getOrNull(position.toNorth()).item) {
                        is Position.Particle -> currentState = Position.Particle(northItem.count)
                        is Position.Start -> currentState = Position.Particle(1)
                        else -> Unit
                    }

                    val eastItem = plane.getOrNull(position.toEast()).item
                    val northEastItem = plane.getOrNull(position.toNorthEast()).item
                    if (eastItem is Position.Splitter && northEastItem is Position.Particle) {
                        currentState = when (currentState) {
                            is Position.Nothing -> Position.Particle(northEastItem.count)
                            is Position.Particle -> Position.Particle(currentState.count + northEastItem.count)
                            else -> throw IllegalStateException("Bug!")
                        }
                    }

                    val westItem = plane.getOrNull(position.toWest()).item
                    val northWestItem = plane.getOrNull(position.toNorthWest()).item
                    if (westItem is Position.Splitter && northWestItem is Position.Particle) {
                        currentState = when (currentState) {
                            is Position.Nothing -> Position.Particle(northWestItem.count)
                            is Position.Particle -> Position.Particle(currentState.count + northWestItem.count)
                            else -> throw IllegalStateException("Bug!")
                        }
                    }
                }

                else -> Unit
            }

            currentState
        }

        println(
            plane.sumOf(
                yProgression = plane.yMax - 1 until plane.yMax
            ) { (item, _) ->
                when (item) {
                    is Position.Particle -> item.count
                    else -> 0
                }
            }
        )
    }

    sealed interface Position {
        data object Nothing : Position {
            override fun toString(): String = "."
        }

        data object Start : Position {
            override fun toString(): String = "S"
        }

        data object Splitter : Position {
            override fun toString(): String = "^"
        }

        data class Particle(val count: Long) : Position {
            override fun toString(): String = "|"
        }
    }
}
