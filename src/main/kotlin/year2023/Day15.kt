package year2023

import utils.getPuzzleInput
import utils.logMeasureTime
import java.util.*

class Day15 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day15().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day15().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        println(
            lines.first().split(",").sumOf {
                getHashValue(it)
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val operations = parseOperations(lines.first())
        val boxes = List<MutableList<Lens>>(256) { mutableListOf() }

        operations.forEach { operation ->
            val existingLenses = boxes[operation.box]

            when (operation) {
                is Operation.Add -> {
                    val indexOfExisting = existingLenses.indexOfFirst { it.label == operation.label }.takeIf { it != -1 }

                    if (indexOfExisting != null) {
                        existingLenses.removeAt(indexOfExisting)
                        existingLenses.add(
                            indexOfExisting,
                            Lens(
                                label = operation.label,
                                focalLength = operation.focalLength,
                            )
                        )
                    } else {
                        existingLenses.add(
                            Lens(
                                label = operation.label,
                                focalLength = operation.focalLength,
                            )
                        )
                    }
                }

                is Operation.Remove -> existingLenses.removeAll { it.label == operation.label }
            }
        }

        println(
            boxes.withIndex().sumOf { (boxIndex, lenses) ->
                lenses.withIndex().sumOf { (lensIndex, lens) ->
                    (boxIndex + 1) * (lensIndex + 1) * lens.focalLength
                }
            }
        )
    }

    private fun parseOperations(first: String): List<Operation> = first.split(",").map {
        if (it.contains("-")) {
            val label = it.substringBefore("-")
            Operation.Remove(
                box = getHashValue(label),
                label = label,
            )
        } else {
            val label = it.substringBefore("=")
            Operation.Add(
                box = getHashValue(label),
                label = label,
                focalLength = it.substringAfter("=").toInt(),
            )
        }
    }

    private fun getHashValue(string: String): Int = string.fold(0) { acc, char ->
        ((acc + char.code) * 17) % 256
    }

    private sealed class Operation {

        abstract val box: Int
        abstract val label: String

        data class Remove(
            override val box: Int,
            override val label: String,
        ) : Operation()

        data class Add(
            override val box: Int,
            override val label: String,
            val focalLength: Int,
        ) : Operation()
    }

    private data class Lens(
        val label: String,
        val focalLength: Int,
    )
}