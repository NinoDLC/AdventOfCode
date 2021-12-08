package year2021

import utils.logMeasureTime
import java.io.File

class Day08 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day08.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day08().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day08().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        lines.map { it.split(" | ")[1] }.fold(0) { acc: Int, output: String ->
            acc + output.split(" ").count {
                it.length == 2 // 1
                    || it.length == 4 // 4
                    || it.length == 3 // 7
                    || it.length == 7 // 8
            }
        }.let {
            println(it)
        }
    }

    private fun partTwo(lines: List<String>) {
        lines.fold(0) { acc: Int, line: String ->
            acc + getSumOfOutputValues(line)
        }.let {
            println(it)
        }
    }

    private fun getSumOfOutputValues(line: String): Int {
        var finalNumber = ""

        val parts = line.split(" | ")

        val signals = parts[0].split(" ")
        val outputs = parts[1].split(" ")

        // Example :
        // 1 -> ab
        // 4 -> abcd
        // 7 -> abc
        // 8 -> abcdefg
        val decoders = mutableMapOf<Int, List<Char>>()
        (signals + outputs).forEach {
            // Lookup
            when (it.length) {
                2 -> decoders[1] = it.toList()
                4 -> decoders[4] = it.toList()
                3 -> decoders[7] = it.toList()
                7 -> decoders[8] = it.toList()
            }
        }

        outputs.forEach { output ->
            finalNumber += when (output.length) {
                2 -> 1
                4 -> 4
                3 -> 7
                7 -> 8
                else -> computeValue(decoders, output)
            }
        }

        return finalNumber.toInt()
    }

    private fun computeValue(decoders: Map<Int, List<Char>>, output: String): Int {
        val outputAsList = output.toList()

        when (output.length) {
            5 -> { // possibles : 2 / 3 / 5
                val onePattern = decoders[1]
                val sevenPattern = decoders[7]
                if (onePattern != null && outputAsList.containsAll(onePattern)
                    || sevenPattern != null && outputAsList.containsAll(sevenPattern)
                ) {
                    return 3
                }

                // possibles : 2 / 5
                val fourPattern = decoders[4]
                if (fourPattern != null && fourPattern.count { outputAsList.contains(it) } == 2) {
                    return 2
                }

                return 5
            }
            6 -> { // possibles : 0 / 6 / 9
                val onePattern = decoders[1]
                val sevenPattern = decoders[7]
                if (onePattern != null && !outputAsList.containsAll(onePattern)
                    || sevenPattern != null && !outputAsList.containsAll(sevenPattern)
                ) {
                    return 6
                }

                // possibles : 0 / 9
                val fourPattern = decoders[4]
                if (fourPattern != null && outputAsList.containsAll(fourPattern)) {
                    return 9
                }

                return 0
            }
            else -> throw IllegalStateException("WTF is this ? [$output]")
        }
    }
}