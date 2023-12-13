package year2023

import utils.Plane
import utils.getPuzzleInput
import utils.logMeasureTime

class Day13 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

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
        val patterns = lines.joinToString("\n").split("\n\n").map { Plane.of(it.split("\n")) }

        println(
            patterns.sumOf { pattern ->
                getPatternSummary(pattern)?.value ?: throw IllegalStateException("Can't find a mirror index for pattern:\n$pattern")
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val patterns = lines.joinToString("\n").split("\n\n").map { Plane.of(it.split("\n")) }

        println(
            patterns.sumOf { pattern ->
                val initialPatternSummary = getPatternSummary(pattern)
                (0 until pattern.yMax).forEach { y ->
                    (0 until pattern.xMax).forEach { x ->
                        invertValue(pattern, x, y)

                        val patternSummary = getPatternSummary(pattern = pattern, excludedSummary = initialPatternSummary)

                        if (patternSummary != null) {
                            return@sumOf patternSummary.value
                        }

                        invertValue(pattern, x, y)
                    }
                }

                throw IllegalStateException("Can't find a new mirror index after smudge fix for pattern:\n$pattern")
            }
        )
    }

    private fun invertValue(pattern: Plane<Char>, x: Int, y: Int) {
        pattern.transform(x, y) {
            when (it.item) {
                '#' -> '.'
                '.' -> '#'
                else -> throw IllegalStateException("Unknown character: ${it.item}")
            }
        }
    }

    private fun getPatternSummary(
        pattern: Plane<Char>,
        excludedSummary: PatternSummary? = null,
    ): PatternSummary? {
        val verticalMirrorIndex = getMirrorIndex(
            max = pattern.xMax,
            excludedIndex = excludedSummary?.index?.takeIf { excludedSummary is PatternSummary.Vertical },
        ) { firstIndex, secondIndex ->
            pattern.getColumn(firstIndex).items.map { it.item } == pattern.getColumn(secondIndex).items.map { it.item }
        }

        if (verticalMirrorIndex != null) {
            return PatternSummary.Vertical(index = verticalMirrorIndex)
        }

        val horizontalMirrorIndex = getMirrorIndex(
            max = pattern.yMax,
            excludedIndex = excludedSummary?.index?.takeIf { excludedSummary is PatternSummary.Horizontal }
        ) { firstIndex, secondIndex ->
            pattern.getRow(firstIndex).items.map { it.item } == pattern.getRow(secondIndex).items.map { it.item }
        }

        if (horizontalMirrorIndex != null) {
            return PatternSummary.Horizontal(index = horizontalMirrorIndex)
        }

        return null
    }

    private fun getMirrorIndex(
        max: Int,
        excludedIndex: Int?,
        isMatching: (firstIndex: Int, secondIndex: Int) -> Boolean,
    ): Int? {
        var index = 0

        while (index + 1 < max) {
            val isMirroringAtIndex = isMirroringAtIndex(index, max, isMatching)

            if (isMirroringAtIndex && index != excludedIndex) {
                return index
            } else {
                index++
            }
        }

        return null
    }

    private fun isMirroringAtIndex(
        index: Int,
        max: Int,
        isMatching: (firstIndex: Int, secondIndex: Int) -> Boolean,
    ): Boolean {
        var upIndex = index
        var downIndex = index + 1

        while (upIndex >= 0 && downIndex < max) {
            if (!isMatching(upIndex, downIndex)) {
                return false
            }
            upIndex--
            downIndex++
        }

        return true
    }

    private sealed class PatternSummary {

        abstract val index: Int
        abstract val value: Int

        data class Vertical(
            override val index: Int,
        ) : PatternSummary() {
            override val value: Int
                get() = index + 1
        }

        data class Horizontal(
            override val index: Int,
        ) : PatternSummary() {
            override val value: Int
                get() = (index + 1) * 100
        }
    }
}