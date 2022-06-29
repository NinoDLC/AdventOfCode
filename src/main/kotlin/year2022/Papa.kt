package year2022

import utils.logMeasureTime
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Papa {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val frenchDict = File("data/2022/french_dict.txt").readLines()

            println("=== Building Tree Dict... ===")
            val frenchTreeDict = logMeasureTime {
                Papa().buildFrenchTreeDict(frenchDict)
            }
            println()

            println("=== Finding possible words... ===")
            logMeasureTime {
                Papa().findFrenchWords(frenchTreeDict)
            }
            println()
        }
    }

    var crawled = 0

    private fun buildFrenchTreeDict(lines: List<String>): Node {
        val treeDict = Node(null, mutableListOf(), false)
        var wordsDone = 0
        var lettersDone = 0
        var nodesCreated = 0

        lines.map {
            it.lowercase(Locale.FRENCH)
        }.forEach { line ->
            var currentNode = treeDict
            line.forEachIndexed { index, char ->
                currentNode = currentNode.children.find { char == it.char } ?: Node(
                    char = char,
                    children = mutableListOf(),
                    isWord = line.length == index + 1
                ).also {
                    currentNode.children.add(it)
                    nodesCreated++
                }

                lettersDone++
            }

            wordsDone++
        }

        println("wordsDone = $wordsDone, lettersDone = $lettersDone, nodesCreated = $nodesCreated")

        return treeDict
    }

    private fun findFrenchWords(frenchTreeDict: Node) {
        val input = arrayOf(
            arrayOf('O', 'S', 'K', 'T'),
            arrayOf('O', 'S', 'R', 'U'),
            arrayOf('E', 'U', 'E', 'C'),
            arrayOf('E', 'L', 'A', 'H')
        )

        val words = HashMap<String, Int>()

        val maxX = input.maxOf { it.size - 1 }
        val maxY = input.size - 1

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                println("Words starting at [x=$x,y=$y] (${input[y][x]}): ")
                crawl(frenchTreeDict, words, "", input, x, y, maxX, maxY, listOf(Position(x, y)))
            }
        }

        println("Crawled count: $crawled")
        println("Unique words found: ${words.size}")
        println("Words found: ${words.values.sum()}")
        println("Words:")
        words.toSortedMap().forEach {
            print(it.key)
            val spaces = 10 - it.key.length
            for (i in 0..spaces) {
                print(" ")
            }
            println("(${it.value} times)")
        }
    }

    private fun crawl(
        frenchTreeDict: Node,
        words: HashMap<String, Int>,
        wordSoFar: String,
        input: Array<Array<Char>>,
        x: Int,
        y: Int,
        maxX: Int,
        maxY: Int,
        previousPositions: List<Position>
    ) {
        crawled++
        val newWord = wordSoFar + input[y][x].lowercase()

        val (dictContainsWord, canContinue) = dictContainsWord(frenchTreeDict, newWord)

        if (newWord.length >= 4 && dictContainsWord) {
            println(newWord)
            words[newWord] = words.getOrDefault(newWord, 0) + 1
        }

        if (canContinue) {
            val newPositions = listOf(
                Position(x = x + 1, y = y),     // East
                Position(x = x + 1, y = y + 1), // SE
                Position(x = x, y = y + 1),     // South
                Position(x = x - 1, y = y + 1), // SW
                Position(x = x - 1, y = y),     // West
                Position(x = x - 1, y = y - 1), // NW
                Position(x = x, y = y - 1),     // North
                Position(x = x + 1, y = y - 1), // NE
            )

            newPositions.forEach { newPosition ->
                if (newPosition.x in 0..maxX
                    && newPosition.y in 0..maxY
                    && !previousPositions.contains(newPosition)
                ) {
                    crawl(
                        frenchTreeDict = frenchTreeDict,
                        words = words,
                        wordSoFar = newWord,
                        input = input,
                        x = newPosition.x,
                        y = newPosition.y,
                        maxX = maxX,
                        maxY = maxY,
                        previousPositions = previousPositions + newPosition
                    )
                }
            }
        }
    }

    /**
     * @return (dictContainsWord, canContinue)
     */
    private fun dictContainsWord(frenchTreeDict: Node, word: String): Pair<Boolean, Boolean> {
        var node = frenchTreeDict

        word.forEach { char ->
            node = node.children.find { it.char == char } ?: return false to false
        }

        return node.isWord to node.children.isNotEmpty()
    }

    data class Node(
        val char: Char?,
        val children: MutableList<Node>,
        val isWord: Boolean
    )

    data class Position(
        val x: Int,
        val y: Int
    )
}