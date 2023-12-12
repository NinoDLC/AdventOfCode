package year2023

import utils.getPuzzleInput
import utils.logMeasureTime
import year2023.Day12.ConditionStatus.*

class Day12 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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

    private val possibleArrangementsCache = mutableMapOf<Triple<List<ConditionStatus>, List<Int>, Int>, Long>()

    private fun partOne(lines: List<String>) {
        val conditionRecords = parseConditionRecords(lines)

        println(
            conditionRecords.sumOf { conditionRecord ->
                countPossibleArrangements(conditionRecord.conditionStatuses, conditionRecord.damages)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val conditionRecords = parseConditionRecords(lines)
        val unfoldedConditionRecords = unfoldConditionRecords(conditionRecords)

        println(
            unfoldedConditionRecords.sumOf { conditionRecord ->
                countPossibleArrangements(conditionRecord.conditionStatuses, conditionRecord.damages)
            }
        )
    }

    private fun parseConditionRecords(lines: List<String>): List<ConditionRecord> = lines.map { line ->
        ConditionRecord(
            conditionStatuses = line.substringBefore(" ").map { char ->
                ConditionStatus.entries.find { it.char == char } ?: throw IllegalStateException("Unknown char: $char")
            },
            damages = line.substringAfter(" ").split(",").map { it.toInt() },
        )
    }

    private fun unfoldConditionRecords(conditionRecords: List<ConditionRecord>): List<ConditionRecord> =
        conditionRecords.map { conditionRecord ->
            ConditionRecord(
                conditionStatuses = (0 until 5).flatMap {
                    if (it == 4) {
                        conditionRecord.conditionStatuses
                    } else {
                        conditionRecord.conditionStatuses + UNKNOWN
                    }
                },
                damages = (0 until 5).flatMap {
                    conditionRecord.damages
                }
            )
        }

    private fun countPossibleArrangements(
        conditionStatuses: List<ConditionStatus>,
        damages: List<Int>,
        initialDamageCount: Int = 0,
    ): Long {
        var index = 0
        var damageCount = initialDamageCount
        var damageIndex = 0

        val cacheKey = Triple(conditionStatuses, damages, initialDamageCount)
        possibleArrangementsCache[cacheKey]?.let { possibleArrangements ->
            return possibleArrangements
        }

        while (index < conditionStatuses.size) {
            val status = conditionStatuses[index]

            when (status) {
                UNKNOWN -> {
                    val possibleArrangementsIfDamaged = countPossibleArrangements(
                        conditionStatuses = conditionStatuses.mapIndexed { conditionStatusIndex, conditionStatus ->
                            if (index == conditionStatusIndex) {
                                DAMAGED
                            } else {
                                conditionStatus
                            }
                        }.drop(index),
                        damages = damages.drop(damageIndex),
                        initialDamageCount = damageCount,
                    )

                    val possibleArrangementsIfOperational = countPossibleArrangements(
                        conditionStatuses = conditionStatuses.mapIndexed { conditionStatusIndex, conditionStatus ->
                            if (index == conditionStatusIndex) {
                                OPERATIONAL
                            } else {
                                conditionStatus
                            }
                        }.drop(index),
                        damages = damages.drop(damageIndex),
                        initialDamageCount = damageCount,
                    )

                    return (possibleArrangementsIfDamaged + possibleArrangementsIfOperational).also {
                        possibleArrangementsCache[cacheKey] = it
                    }
                }

                OPERATIONAL -> {
                    if (damageCount != 0) {
                        val damage = damages[damageIndex++]

                        if (damage != damageCount) {
                            possibleArrangementsCache[cacheKey] = 0
                            return 0
                        } else {
                            damageCount = 0
                        }
                    }
                }

                DAMAGED -> {
                    damageCount++
                    if (damageIndex >= damages.size || damageCount > damages[damageIndex]) {
                        possibleArrangementsCache[cacheKey] = 0
                        return 0
                    }
                }
            }

            index++
        }

        return if (
            damageIndex == damages.size - 1 && damageCount == damages[damageIndex] || // If finishing with DAMAGED
            damageIndex == damages.size && damageCount == 0 // If finishing with OPERATIONAL
        ) {
            1L
        } else {
            0
        }.also {
            possibleArrangementsCache[cacheKey] = it
        }
    }

    private data class ConditionRecord(
        val conditionStatuses: List<ConditionStatus>,
        val damages: List<Int>,
    ) {
        fun getConditionStatusesString() = conditionStatuses.joinToString(separator = "") { it.char.toString() }

        override fun toString(): String =
            getConditionStatusesString() +
                " " +
                damages.joinToString(separator = ",")
    }

    private enum class ConditionStatus(val char: Char) {
        UNKNOWN('?'),
        OPERATIONAL('.'),
        DAMAGED('#'),
    }
}