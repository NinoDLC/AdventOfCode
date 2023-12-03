package year2023

import utils.getPuzzleInput
import utils.logMeasureTime

class Day03 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day03().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day03().partTwo(lines)
            }
            println()
        }

        private val NUMBER_REGEX = "[0-9]+".toRegex()
        private val SYMBOL_REGEX = "[%*#&\$@/=+-]".toRegex()
    }

    private fun partOne(lines: List<String>) {
        val indexedNumbers: List<IndexedNumber> = getIndexedNumbers(lines)
        val indexedSymbols: List<IndexedSymbol> = getIndexedSymbols(lines)

        println(
            indexedNumbers.filter { indexedNumber ->
                indexedSymbols.any { indexedSymbol ->
                    isIndexedNumberNearSymbol(indexedSymbol, indexedNumber)
                }
            }.sumOf {
                it.value
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val indexedNumbers: List<IndexedNumber> = getIndexedNumbers(lines)
        val indexedSymbols: List<IndexedSymbol> = getIndexedSymbols(lines)

        println(
            indexedSymbols.sumOf { indexedSymbol ->
                if (indexedSymbol.symbol == '*') {
                    val correspondingNumbers = indexedNumbers.filter { indexedNumber ->
                        isIndexedNumberNearSymbol(indexedSymbol, indexedNumber)
                    }

                    if (correspondingNumbers.size == 2) {
                        correspondingNumbers.first().value * correspondingNumbers.last().value
                    } else {
                        0
                    }
                } else {
                    0
                }
            }
        )
    }

    private fun getIndexedNumbers(lines: List<String>): List<IndexedNumber> = lines.mapIndexed { index, line ->
        NUMBER_REGEX.findAll(line).map {
            IndexedNumber(
                value = it.value.toInt(),
                xes = (it.range.first - 1)..(it.range.last + 1),
                y = index,
            )
        }.toList()
    }.flatten()

    private fun getIndexedSymbols(lines: List<String>): List<IndexedSymbol> = lines.mapIndexed { index, line ->
        SYMBOL_REGEX.findAll(line).map {
            IndexedSymbol(
                symbol = if (it.value.length > 1) {
                    throw IllegalStateException("Error! ${it.value} is not a symbol (unique char)")
                } else {
                    it.value.first()
                },
                x = it.range.first,
                y = index,
            )
        }.toList()
    }.flatten()

    private fun isIndexedNumberNearSymbol(
        indexedSymbol: IndexedSymbol,
        indexedNumber: IndexedNumber
    ) = (indexedNumber.y == indexedSymbol.y || indexedNumber.y - 1 == indexedSymbol.y || indexedNumber.y + 1 == indexedSymbol.y)
        && indexedSymbol.x in indexedNumber.xes

    data class IndexedNumber(
        val value: Int,
        val xes: IntRange,
        val y: Int,
    )

    data class IndexedSymbol(
        val symbol: Char,
        val x: Int,
        val y: Int,
    )
}