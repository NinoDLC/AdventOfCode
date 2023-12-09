package year2015

import utils.getPuzzleInput
import utils.logMeasureTime

class Day08 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
        println(
            lines.sumOf { line ->
                val memoryString = line.drop(1).dropLast(1)
                var charsInMemory = 0
                var i = 0

                while (i < memoryString.length) {
                    if (memoryString[i++] == '\\') {
                        when (val nextChar = memoryString[i++]) {
                            'x' -> {
                                i += 2 // Consume the ASCII code
                                charsInMemory++
                            }

                            '\\',
                            '\"' -> charsInMemory++

                            else -> throw IllegalStateException("Unknown escaped char $nextChar at index $i for string $line")
                        }
                    } else {
                        charsInMemory++
                    }
                }

                line.length - charsInMemory
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        println(
            lines.sumOf { line ->
                val newLineBuilder = StringBuilder()
                var i = 0

                while (i < line.length) {
                    when (val char = line[i++]) {
                        '\\' -> {
                            newLineBuilder.append("\\\\")
                            when (val nextChar = line[i++]) {
                                'x' -> {
                                    // Consume the ASCII code
                                    newLineBuilder.append('x')
                                    newLineBuilder.append(line[i++]) // first digit
                                    newLineBuilder.append(line[i++]) // second digit
                                }

                                '\\' -> newLineBuilder.append("\\\\")
                                '\"' -> newLineBuilder.append("\\\"")

                                else -> throw IllegalStateException("Unknown escaped char $nextChar at index $i for string $line")
                            }
                        }

                        '"' -> newLineBuilder.append("\\\"")
                        else -> newLineBuilder.append(char)
                    }
                }

                // +2 because of first and last double quote
                newLineBuilder.length + 2 - line.length
            }
        )
    }
}