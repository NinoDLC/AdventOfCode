package year2015

import utils.getPuzzleInput
import utils.logMeasureTime

class Day07 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day07().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day07().partTwo(lines)
            }
            println()
        }

        private val ASSIGN_REGEX = "^([0-9]+|[a-z]+) -> ([a-z]+)$".toRegex()
        private val AND_REGEX = "^([0-9]+|[a-z]+) AND ([0-9]+|[a-z]+) -> ([a-z]+)$".toRegex()
        private val OR_REGEX = "^([0-9]+|[a-z]+) OR ([0-9]+|[a-z]+) -> ([a-z]+)$".toRegex()
        private val NOT_REGEX = "^NOT ([a-z]+) -> ([a-z]+)$".toRegex()
        private val L_SHIFT_REGEX = "^([0-9]+|[a-z]+) LSHIFT ([0-9]+) -> ([a-z]+)$".toRegex()
        private val R_SHIFT_REGEX = "^([0-9]+|[a-z]+) RSHIFT ([0-9]+) -> ([a-z]+)$".toRegex()
    }

    private val variableCache = mutableMapOf<Variable, UShort>()

    private fun partOne(lines: List<String>) {
        val operations = parseOperations(lines)
        println(getValue(operations, Variable.Named("a")))
    }

    private fun partTwo(lines: List<String>) {
        val operations = parseOperations(lines).toMutableList()

        val bValue = getValue(operations, Variable.Named("a"))
        operations.removeAll { it.variable.name == "b" }
        operations.add(
            Operation.Assign(
                value = Variable.Value(bValue),
                variable = Variable.Named("b"),
            ),
        )
        variableCache.clear()

        println(getValue(operations, Variable.Named("a")))
    }

    private fun getValue(
        operations: List<Operation>,
        variable: Variable,
    ): UShort {
        return when (variable) {
            is Variable.Value -> variable.value
            is Variable.Named -> variableCache[variable] ?: run {
                val operation = requireNotNull(operations.find { it.variable == variable }) { "Couldn't find variable: $variable" }

                computeOperationValue(operations, operation).also {
                    variableCache[variable] = it
                }
            }
        }
    }

    private fun computeOperationValue(
        operations: List<Operation>,
        operation: Operation,
    ) = when (operation) {
        is Operation.Assign -> getValue(operations, operation.value)
        is Operation.And -> getValue(operations, operation.input1) and getValue(operations, operation.input2)
        is Operation.Or -> getValue(operations, operation.input1) or getValue(operations, operation.input2)
        is Operation.Not -> getValue(operations, operation.input).inv()
        is Operation.LShift -> getValue(operations, operation.input).toUInt().shl(operation.lShiftValue.value.toInt()).toUShort()
        is Operation.RShift -> getValue(operations, operation.input).toUInt().shr(operation.rShiftValue.value.toInt()).toUShort()
    }

    private fun parseOperations(lines: List<String>): List<Operation> {
        val operations = lines.map { line ->
            ASSIGN_REGEX.find(line)?.let { matchResult ->
                val (value, variable) = matchResult.destructured
                Operation.Assign(
                    value = parseVariable(value),
                    variable = Variable.Named(variable)
                )
            } ?: AND_REGEX.find(line)?.let { matchResult ->
                val (input1, input2, variable) = matchResult.destructured
                Operation.And(
                    input1 = parseVariable(input1),
                    input2 = parseVariable(input2),
                    variable = Variable.Named(variable),
                )
            } ?: OR_REGEX.find(line)?.let { matchResult ->
                val (input1, input2, variable) = matchResult.destructured
                Operation.Or(
                    input1 = parseVariable(input1),
                    input2 = parseVariable(input2),
                    variable = Variable.Named(variable),
                )
            } ?: NOT_REGEX.find(line)?.let { matchResult ->
                val (input, variable) = matchResult.destructured
                Operation.Not(
                    input = Variable.Named(input),
                    variable = Variable.Named(variable),
                )
            } ?: L_SHIFT_REGEX.find(line)?.let { matchResult ->
                val (input, lShiftValue, variable) = matchResult.destructured
                Operation.LShift(
                    input = parseVariable(input),
                    lShiftValue = Variable.Value(lShiftValue.toUShort()),
                    variable = Variable.Named(variable),
                )
            } ?: R_SHIFT_REGEX.find(line)?.let { matchResult ->
                val (input, rShiftValue, variable) = matchResult.destructured
                Operation.RShift(
                    input = parseVariable(input),
                    rShiftValue = Variable.Value(rShiftValue.toUShort()),
                    variable = Variable.Named(variable),
                )
            } ?: throw IllegalStateException("Unknown operation: $line")
        }
        return operations
    }

    private fun parseVariable(variable: String): Variable = variable.toUShortOrNull()?.let {
        Variable.Value(it)
    } ?: Variable.Named(variable)

    private sealed class Operation {
        abstract val variable: Variable.Named

        data class Assign(
            val value: Variable,
            override val variable: Variable.Named,
        ) : Operation()

        data class And(
            val input1: Variable,
            val input2: Variable,
            override val variable: Variable.Named,
        ) : Operation()

        data class Or(
            val input1: Variable,
            val input2: Variable,
            override val variable: Variable.Named,
        ) : Operation()

        data class Not(
            val input: Variable.Named,
            override val variable: Variable.Named,
        ) : Operation()

        data class LShift(
            val input: Variable,
            val lShiftValue: Variable.Value,
            override val variable: Variable.Named,
        ) : Operation()

        data class RShift(
            val input: Variable,
            val rShiftValue: Variable.Value,
            override val variable: Variable.Named,
        ) : Operation()
    }

    private sealed class Variable {
        data class Value(val value: UShort) : Variable()
        data class Named(val name: String) : Variable()
    }
}