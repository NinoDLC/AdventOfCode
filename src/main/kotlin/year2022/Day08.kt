package year2022

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
        val trees = getTrees(lines)

        println(
            trees.count { entry ->
                val treesOnTheSameLineOnTheLeft = trees.filterKeys {
                    it.y == entry.key.y && it.x < entry.key.x
                }
                val treesOnTheSameLineOnTheRight = trees.filterKeys {
                    it.y == entry.key.y && it.x > entry.key.x
                }
                val treesOnTheSameLineOnTheTop = trees.filterKeys {
                    it.x == entry.key.x && it.y < entry.key.y
                }
                val treesOnTheSameLineOnTheBottom = trees.filterKeys {
                    it.x == entry.key.x && it.y > entry.key.y
                }

                treesOnTheSameLineOnTheLeft.all { entry.value > it.value }
                    || treesOnTheSameLineOnTheRight.all { entry.value > it.value }
                    || treesOnTheSameLineOnTheTop.all { entry.value > it.value }
                    || treesOnTheSameLineOnTheBottom.all { entry.value > it.value }
            }
        )
    }

    private fun partTwo(lines: List<String>) {
        val trees = getTrees(lines)

        val xMax = trees.maxOf { it.key.x }
        val yMax = trees.maxOf { it.key.y }

        println(
            trees.maxOf { evaluatedTree ->
                if (evaluatedTree.key.x == 0 || evaluatedTree.key.x == xMax || evaluatedTree.key.y == 0 || evaluatedTree.key.y == yMax) {
                    return@maxOf 0
                }

                val sceneScoreForLeft = List(evaluatedTree.key.x) { currentX ->
                    trees.entries.find { it.key.x == evaluatedTree.key.x - (currentX + 1) && it.key.y == evaluatedTree.key.y }
                }.indexOfFirst {
                    it!!.value >= evaluatedTree.value
                }.let {
                    if (it == -1) {
                        evaluatedTree.key.x
                    } else {
                        it + 1
                    }
                }

                val sceneScoreForRight = List(xMax - evaluatedTree.key.x) { currentX ->
                    trees.entries.find { it.key.x == evaluatedTree.key.x + currentX + 1 && it.key.y == evaluatedTree.key.y }
                }.indexOfFirst {
                    it!!.value >= evaluatedTree.value
                }.let {
                    if (it == -1) {
                        xMax - evaluatedTree.key.x
                    } else {
                        it + 1
                    }
                }

                val sceneScoreForTop = List(evaluatedTree.key.y) { currentY ->
                    trees.entries.find { it.key.x == evaluatedTree.key.x && it.key.y == evaluatedTree.key.y - (currentY + 1) }
                }.indexOfFirst {
                    it!!.value >= evaluatedTree.value
                }.let {
                    if (it == -1) {
                        evaluatedTree.key.y
                    } else {
                        it + 1
                    }
                }

                val sceneScoreForBottom = List(yMax - evaluatedTree.key.y) { currentY ->
                    trees.entries.find { it.key.x == evaluatedTree.key.x && it.key.y == evaluatedTree.key.y + currentY + 1 }
                }.indexOfFirst {
                    it!!.value >= evaluatedTree.value
                }.let {
                    if (it == -1) {
                        yMax - evaluatedTree.key.y
                    } else {
                        it + 1
                    }
                }

                sceneScoreForLeft * sceneScoreForRight * sceneScoreForTop * sceneScoreForBottom
            }
        )
    }

    private fun getTrees(lines: List<String>): MutableMap<Position, Int> {
        val trees = mutableMapOf<Position, Int>()

        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                trees[Position(x, y)] = char.toString().toInt()
            }
        }

        return trees
    }

    private data class Position(
        val x: Int,
        val y: Int,
    )
}