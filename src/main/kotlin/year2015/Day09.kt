package year2015

import utils.getPuzzleInput
import utils.logMeasureTime
import utils.permutations

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

        private val REGEX_DISTANCES = "([a-zA-Z]+) to ([a-zA-Z]+) = ([0-9]+)".toRegex()
    }

    private fun partOne(lines: List<String>) {
        val distances: Map<Pair<City, City>, Long> = parseDistances(lines)
        val cities: List<City> = getUniqueCities(distances)
        val pathDistances: Sequence<Long> = getPathDistances(cities, distances)

        println(pathDistances.min())
    }

    private fun partTwo(lines: List<String>) {
        val distances: Map<Pair<City, City>, Long> = parseDistances(lines)
        val cities: List<City> = getUniqueCities(distances)
        val pathDistances: Sequence<Long> = getPathDistances(cities, distances)

        println(pathDistances.max())
    }

    private fun parseDistances(lines: List<String>): Map<Pair<City, City>, Long> = lines.associate { line ->
        val (city1, city2, distance) = REGEX_DISTANCES.find(line)!!.destructured

        City(city1) to City(city2) to distance.toLong()
    }

    private fun getUniqueCities(distances: Map<Pair<City, City>, Long>) =
        distances.keys
            .flatMap { listOf(it.first, it.second) }
            .distinct()

    private fun getPathDistances(
        cities: List<City>,
        distances: Map<Pair<City, City>, Long>
    ): Sequence<Long> =
        cities
            .permutations()
            .map { paths: List<City> ->
                paths.zipWithNext { currentCity, nextCity ->
                    requireNotNull(distances[currentCity to nextCity] ?: distances[nextCity to currentCity]) {
                        "Path between $currentCity and $nextCity is not known in ${distances.entries.joinToString("\n")}"
                    }
                }.sum()
            }

    @JvmInline
    private value class City(val value: String)
}