package year2023

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utils.getPuzzleInput
import utils.logMeasureTime

class Day05 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day05().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day05().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) = runBlocking {
        val seedRanges = parseSeeds(lines.first()).map { it..it }
        val mappingsList = parseMappings(lines)

        println(getLowestLocationNumber(seedRanges, mappingsList))
    }

    private fun partTwo(lines: List<String>) = runBlocking {
        val seedRanges = parseSeeds(lines.first()).chunked(2).map {
            it.first() until it.last() + it.first()
        }
        val mappingsList = parseMappings(lines)

        println(getLowestLocationNumber(seedRanges, mappingsList))
    }

    private fun parseSeeds(seeds: String): List<Long> = seeds.split(": ").last().split(" ").map { it.toLong() }

    private fun parseMappings(lines: List<String>): List<List<Mapping>> {
        val mappings = mutableListOf<MutableList<Mapping>>(mutableListOf())
        lines.drop(2).forEach { line ->
            if (line.isEmpty()) {
                mappings.add(mutableListOf())
            } else if (!line.endsWith("map:")) {
                val split = line.split(" ").map { it.toLong() }
                val destinationStart = split[0]
                val sourceStart = split[1]
                val count = split[2]

                mappings.last().add(
                    Mapping(
                        sourceRange = sourceStart until sourceStart + count,
                        delta = destinationStart - sourceStart,
                    )
                )
            }
        }
        return mappings
    }

    private suspend fun getLowestLocationNumber(
        seedRanges: List<LongRange>,
        mappingsList: List<List<Mapping>>
    ): Long = withContext(Dispatchers.Default) {
        seedRanges
            .chunkRanges(1_000_000)
            .map { seedRange ->
                async {
                    seedRange.minOf { seedValue ->
                        getFinalSeedValue(
                            mappingsList = mappingsList,
                            initialSeedValue = seedValue,
                        )
                    }
                }
            }
            .minOf {
                it.await()
            }
    }

    private fun getFinalSeedValue(mappingsList: List<List<Mapping>>, initialSeedValue: Long) =
        mappingsList.fold(initialSeedValue) { newSeedValue, mappings ->
            val correspondingMapping = mappings.find { newSeedValue in it.sourceRange }

            (correspondingMapping?.delta?.plus(newSeedValue) ?: newSeedValue)
        }

    private fun List<LongRange>.chunkRanges(maxElements: Long): List<LongRange> = map { range ->
        val itemCount = range.last - range.first
        val chunks = itemCount / maxElements

        if (chunks == 0L) {
            listOf(range)
        } else {
            List(chunks.toInt()) { chunkIndex ->
                (range.first + chunkIndex * maxElements)..(range.first + (chunkIndex + 1) * maxElements).coerceAtMost(range.last)
            }
        }
    }.flatten()

    private data class Mapping(
        val sourceRange: LongRange,
        val delta: Long,
    )
}