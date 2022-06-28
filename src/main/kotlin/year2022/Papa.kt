package year2022

import utils.logMeasureTime
import java.io.File

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

    private fun buildFrenchTreeDict(lines: List<String>): Node {
        val treeDict = Node("", mutableListOf(), false)
        var wordsDone = 0
        var lettersDone = 0
        var nodesCreated = 0

        lines.forEach { line ->
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

        for (x in 0..maxX) {
            for (y in 0..maxY) {
                crawl(frenchTreeDict, words, "", input, x, y, maxX, maxY)
            }
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
        maxY: Int
    ) {

        val newWord = wordSoFar + input[y][x]

        val (dictContainsWord, canContinue) = dictContainsWord(frenchTreeDict, newWord)

        if (newWord.length >= 4 && dictContainsWord) {
            println("Found $newWord")
            words[newWord] = words.getOrDefault(newWord, 0) + 1
        }

        // Order: N, NW, NE, S, SW, SE, W, E

        if (canContinue) {
            // North
            if (y - 1 >= 0) {
                crawl(frenchTreeDict, words, newWord, input, x, y - 1, maxX, maxY)

                // NW
                if (x - 1 >= 0) {
                    crawl(frenchTreeDict, words, newWord, input, x - 1, y - 1, maxX, maxY)
                }

                // NE
                if (x + 1 <= maxX) {
                    crawl(frenchTreeDict, words, newWord, input, x + 1, y - 1, maxX, maxY)
                }
            }

            // South
            if (y + 1 <= maxY) {
                crawl(frenchTreeDict, words, newWord, input, x, y + 1, maxX, maxY)

                // SW
                if (x - 1 >= 0) {
                    crawl(frenchTreeDict, words, newWord, input, x - 1, y + 1, maxX, maxY)
                }

                // SE
                if (x + 1 <= maxX) {
                    crawl(frenchTreeDict, words, newWord, input, x + 1, y + 1, maxX, maxY)
                }
            }

            // West
            if (x - 1 >= 0) {
                crawl(frenchTreeDict, words, newWord, input, x - 1, y, maxX, maxY)
            }

            // East
            if (x + 1 <= maxX) {
                crawl(frenchTreeDict, words, newWord, input, x + 1, y, maxX, maxY)
            }
        }


//        return children.fold(0) { acc, node ->
//            when {
//                node.isWord -> {
//                    val word = "$wordSoFar${node.char}"
//                    println(word)
//                    acc + 1 + crawl(lines, node.children, "$wordSoFar${node.char}")
//                }
//                node.children.isEmpty() -> throw IllegalStateException(node.toString())
//                else -> acc + crawl(lines, node.children, wordSoFar + node.char)
//            }
//        }
    }

    /**
     * @return (dictContainsWord, canContinue)
     */
    private fun dictContainsWord(frenchTreeDict: Node, word: String): Pair<Boolean, Boolean> {
        var node = frenchTreeDict

        word.forEach { char ->
            node = node.children.find { it.char == char } ?: return false to false
        }

        return true to node.children.isNotEmpty()
    }

    data class Node(
        val char: String,
        val children: MutableList<Node>,
        val isWord: Boolean
    )
}