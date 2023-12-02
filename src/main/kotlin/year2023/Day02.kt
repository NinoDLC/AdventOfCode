package year2023

import utils.getPuzzleInput
import utils.logMeasureTime

class Day02 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day02().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day02().partTwo(lines)
            }
            println()
        }

        private val GAME_REGEX = "Game ([0-9]+):".toRegex()
    }

    private fun partOne(lines: List<String>) {
        println(
            countCubePower(lines) { id, cubes ->
                if (cubes.all {
                        it.color == CubeColor.RED && it.count <= 12 ||
                            it.color == CubeColor.GREEN && it.count <= 13 ||
                            it.color == CubeColor.BLUE && it.count <= 14
                    }
                ) {
                    id
                } else {
                    0
                }
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            countCubePower(lines) { _, cubes ->
                val redCount = cubes.filter { it.color == CubeColor.RED }.maxOf { it.count }
                val greenCount = cubes.filter { it.color == CubeColor.GREEN }.maxOf { it.count }
                val blueCount = cubes.filter { it.color == CubeColor.BLUE }.maxOf { it.count }

                redCount * greenCount * blueCount
            }
        )
    }

    private fun countCubePower(lines: List<String>, lambda: (id: Int, cubes: List<Cube>) -> Int): Int = lines.sumOf { line ->
        val id = GAME_REGEX.find(line)!!.groupValues[1].toInt()

        val redCubes: List<Cube> = CubeColor.RED.regex.findAll(line).map { matchResult ->
            Cube(
                count = matchResult.groupValues[1].toInt(),
                color = CubeColor.RED,
            )
        }.toList()
        val greenCubes: List<Cube> = CubeColor.GREEN.regex.findAll(line).map { matchResult ->
            Cube(
                count = matchResult.groupValues[1].toInt(),
                color = CubeColor.GREEN,
            )
        }.toList()
        val blueCubes: List<Cube> = CubeColor.BLUE.regex.findAll(line).map { matchResult ->
            Cube(
                count = matchResult.groupValues[1].toInt(),
                color = CubeColor.BLUE,
            )
        }.toList()

        lambda(id, redCubes + greenCubes + blueCubes)
    }

    private data class Cube(
        val count: Int,
        val color: CubeColor,
    )

    private enum class CubeColor(val regex: Regex) {
        RED("([0-9]+) red".toRegex()),
        GREEN("([0-9]+) green".toRegex()),
        BLUE("([0-9]+) blue".toRegex()),
    }
}