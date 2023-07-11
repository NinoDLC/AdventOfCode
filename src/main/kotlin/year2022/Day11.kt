package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day11 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day11().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day11().partTwo(lines)
            }
            println()
        }

        @Suppress("RegExpRepeatedSpace")
        private val MONKEY_REGEX = """
            Monkey ([0-9]+):
              Starting items: ([0-9, ]+)
              Operation: new = old ([+*]) (old|[0-9]+)
              Test: divisible by ([0-9]+)
                If true: throw to monkey ([0-9]+)
                If false: throw to monkey ([0-9]+)
        """.trimIndent().toRegex(RegexOption.MULTILINE)
    }

    private fun partOne(lines: List<String>) {
        val monkeys = parseMonkeys(lines)

        printMonkeyBusinessLevel(monkeys = monkeys, rounds = 20) {
            it / 3
        }
    }

    private fun partTwo(lines: List<String>) {
        val monkeys = parseMonkeys(lines)
        val primeModulo = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisor }

        printMonkeyBusinessLevel(monkeys = monkeys, rounds = 10_000) {
            it % primeModulo
        }
    }

    private fun printMonkeyBusinessLevel(monkeys: List<Monkey>, rounds: Int, newWorryTransformation: (Long) -> Long) {
        val monkeyInspectionCounts: Array<Long> = Array(monkeys.size) { 0 }
        repeat(rounds) {
            monkeys.forEach { monkey ->
                monkeyInspectionCounts[monkey.index] += monkey.worryLevels.size.toLong()

                monkey.worryLevels.forEach { worryLevel ->
                    val newWorryLevel = newWorryTransformation(monkey.operation.getLevelWorryLevel(worryLevel))
                    if (newWorryLevel % monkey.divisor == 0L) {
                        monkeys[monkey.monkeyIndexForDivisible].worryLevels.add(newWorryLevel)
                    } else {
                        monkeys[monkey.monkeyIndexForNotDivisible].worryLevels.add(newWorryLevel)
                    }
                }
                monkey.worryLevels.clear()
            }
        }

        println(
            monkeyInspectionCounts
                .sortedDescending()
                .take(2)
                .let { it.first() * it.last() }
        )
    }

    private fun parseMonkeys(lines: List<String>): List<Monkey> = lines.chunked(7) {
        it.joinToString(separator = "\n")
    }.map { monkeyDescription ->
        val regexGroupValues = MONKEY_REGEX.find(monkeyDescription)!!.groupValues

        val operator = regexGroupValues[3]
        val operand = regexGroupValues[4]
        Monkey(
            index = regexGroupValues[1].toInt(),
            worryLevels = regexGroupValues[2].split(", ").map { it.toLong() }.toMutableList(),
            operation = when (operator) {
                "*" -> if (operand == "old") {
                    Operation.Squared
                } else {
                    Operation.Multiplication(operand.toLong())
                }

                "+" -> if (operand == "old") {
                    Operation.Multiplication(2)
                } else {
                    Operation.Addition(operand.toLong())
                }

                else -> throw IllegalStateException("Unknown operator : $operator")
            },
            divisor = regexGroupValues[5].toLong(),
            monkeyIndexForDivisible = regexGroupValues[6].toInt(),
            monkeyIndexForNotDivisible = regexGroupValues[7].toInt(),
        )
    }

    private data class Monkey(
        val index: Int,
        val worryLevels: MutableList<Long>,
        val operation: Operation,
        val divisor: Long,
        val monkeyIndexForDivisible: Int,
        val monkeyIndexForNotDivisible: Int,
    )

    private sealed class Operation {
        abstract fun getLevelWorryLevel(value: Long): Long

        data class Addition(val operand: Long) : Operation() {
            override fun getLevelWorryLevel(value: Long): Long = value + operand
        }

        data class Multiplication(val operand: Long) : Operation() {
            override fun getLevelWorryLevel(value: Long): Long = value * operand
        }

        object Squared : Operation() {
            override fun getLevelWorryLevel(value: Long): Long = value * value
        }
    }
}
