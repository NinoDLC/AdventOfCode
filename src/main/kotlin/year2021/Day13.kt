package year2021

import utils.logMeasureTime
import utils.print2DPositions
import java.io.File

class Day13 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day13.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day13().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day13().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val (parsedPositions: Set<Position>, parsedInstructions: List<Instruction>) = parsePositionAndInstructions(lines)

        println(executeInstructions(parsedPositions, parsedInstructions.take(1)).size)
    }

    private fun partTwo(lines: List<String>) {
        val (parsedPositions: Set<Position>, parsedInstructions: List<Instruction>) = parsePositionAndInstructions(lines)

        print2DPositions(executeInstructions(parsedPositions, parsedInstructions).map {
            Pair(it.x, it.y)
        })
    }

    private fun parsePositionAndInstructions(lines: List<String>): Pair<Set<Position>, List<Instruction>> {
        val (positions: List<String>, instructions: List<String>) =
            lines
                .asSequence()
                .filter { it.isNotEmpty() }
                .partition { !it.startsWith("fold") }

        val parsedPositions: Set<Position> = positions.map { position ->
            position.split(",").let {
                Position(
                    x = it.first().toInt(),
                    y = it.last().toInt()
                )
            }
        }.toSet()

        val parsedInstructions: List<Instruction> = instructions.map {
            it.removePrefix("fold along ")
        }.map { instruction ->
            instruction.split("=").let {
                Instruction(
                    axis = if (it.first().first() == 'x') Axis.X else Axis.Y,
                    value = it.last().toInt()
                )
            }
        }
        return Pair(parsedPositions, parsedInstructions)
    }

    private fun executeInstructions(
        parsedPositions: Set<Position>,
        parsedInstructions: List<Instruction>
    ): Set<Position> = parsedInstructions.fold(parsedPositions) { acc: Set<Position>, instruction: Instruction ->
        acc.map { position ->
            if (instruction.axis == Axis.X && instruction.value < position.x) {
                position.copy(x = instruction.value - (position.x - instruction.value))
            } else if (instruction.axis == Axis.Y && instruction.value < position.y) {
                position.copy(y = instruction.value - (position.y - instruction.value))
            } else {
                position
            }
        }.toSet()
    }

    private data class Position(
        val x: Int,
        val y: Int
    )

    private data class Instruction(
        val axis: Axis,
        val value: Int
    )

    private enum class Axis {
        X,
        Y
    }
}