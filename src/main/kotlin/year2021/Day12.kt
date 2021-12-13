package year2021

import utils.logMeasureTime
import java.io.File

class Day12 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day12.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day12().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day12().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val connexions: Set<Connexion> = getConnexions(lines)

        val journeys = mutableListOf<Journey>()

        connexions.filter {
            it.from == "start"
        }.map {
            Journey(visitedConnexions = listOf(it))
        }.forEach {
            journeys.addAll(visit(it, connexions))
        }

        println(journeys.size)
    }

    private fun partTwo(lines: List<String>) {
        val connexions: Set<Connexion> = getConnexions(lines)

        val journeys = mutableListOf<Journey>()

        connexions.filter {
            it.from == "start"
        }.map {
            Journey(visitedConnexions = listOf(it), canGoAnExtraSmallCave = true)
        }.forEach {
            journeys.addAll(visit(it, connexions))
        }

        println(journeys.size)
    }

    private fun getConnexions(lines: List<String>): Set<Connexion> = lines.flatMap { line ->
        line.split("-").let {
            listOf(
                Connexion(
                    from = it.first(),
                    to = it.last(),
                    toSmallCave = it.last().lowercase() == it.last()
                        && it.last() != "start"
                        && it.last() != "end"
                ),
                Connexion(
                    from = it.last(),
                    to = it.first(),
                    toSmallCave = it.first().lowercase() == it.first()
                        && it.first() != "start"
                        && it.first() != "end"
                )
            )
        }
    }.filter {
        it.to != "start" && it.from != "end"
    }.toSet()

    private fun visit(
        journey: Journey,
        allConnexions: Collection<Connexion>
    ): List<Journey> {
        val currentConnexion = journey.visitedConnexions.last()

        if (currentConnexion.to == "end") {
            return listOf(journey)
        }

        val suitableConnexions: List<Connexion> = allConnexions.filter { connexion ->
            if (currentConnexion.to == connexion.from) {
                if (!connexion.toSmallCave) {
                    true
                } else {
                    val duplicateSmallCaveVisits = (journey.visitedConnexions + connexion)
                        .asSequence()
                        .filter { it.toSmallCave }
                        .groupingBy { it.to }
                        .eachCount()
                        .filter { it.value > 1 }

                    duplicateSmallCaveVisits.isEmpty() ||
                        (duplicateSmallCaveVisits.size == 1
                            && duplicateSmallCaveVisits.entries.first().value == 2
                            && journey.canGoAnExtraSmallCave)
                }
            } else {
                false
            }
        }

        val newJourneys = ArrayList<Journey>()

        suitableConnexions.forEach { suitableConnexion ->
            newJourneys.addAll(
                visit(
                    journey = journey.copy(
                        visitedConnexions = journey.visitedConnexions + suitableConnexion
                    ),
                    allConnexions = allConnexions
                )
            )
        }

        return newJourneys
    }

    data class Connexion(
        val from: String,
        val to: String,
        val toSmallCave: Boolean
    )

    data class Journey(
        val visitedConnexions: List<Connexion>,
        val canGoAnExtraSmallCave: Boolean = false
    )
}