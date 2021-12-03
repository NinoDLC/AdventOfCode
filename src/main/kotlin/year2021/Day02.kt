package year2021

import utils.logMeasureTime
import java.io.File

class Day02 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day02.txt").readLines()

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
    }

    private fun partOne(lines: List<String>) {
        val finalPosition = lines.fold(Pair(0, 0)) { position, operation ->
            val command = operation.split(" ")

            when (command[0]) {
                "forward" -> position.copy(first = position.first + command[1].toInt())
                "down" -> position.copy(second = position.second + command[1].toInt())
                "up" -> position.copy(second = position.second - command[1].toInt())
                else -> position
            }
        }
        println(finalPosition.first * finalPosition.second)
    }

    private fun partTwo(lines: List<String>) {
        val finalState = lines.fold(SubmarineState(0, 0, 0)) { submarineState, operation ->
            val command = operation.split(" ")

            when (command[0]) {
                "forward" -> submarineState.copy(
                    x = submarineState.x + command[1].toInt(),
                    y = submarineState.y + submarineState.aim * command[1].toInt()
                )
                "down" -> submarineState.copy(aim = submarineState.aim + command[1].toInt())
                "up" -> submarineState.copy(aim = submarineState.aim - command[1].toInt())
                else -> submarineState
            }
        }
        println(finalState.x * finalState.y)
    }

    private data class SubmarineState(
        val x: Int,
        val y: Int,
        val aim: Int
    )
}