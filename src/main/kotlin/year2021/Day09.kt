package year2021

import utils.logMeasureTime
import java.io.File

class Day09 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day09.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day09().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day09().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        var score = 0

        lines.forEachIndexed { y, line ->
            line.map { it.toString().toInt() }.forEachIndexed { x, value ->
                if ((x == 0 || value < line[x - 1].toString().toInt()) // Left
                    && (x == 99 || value < line[x + 1].toString().toInt()) // Right
                    && (y == 0 || value < lines[y - 1][x].toString().toInt()) // Top
                    && (y == 99 || value < lines[y + 1][x].toString().toInt()) // Bottom
                ) {
                    score += (1 + value)
                }
            }
        }

        println(score)
    }

    private fun partTwo(lines: List<String>) {
        val map: Array<Array<Int>> = Array(lines.size) { line ->
            lines[line].map { it.toString().toInt() }.toTypedArray()
        }

        val sizes = ArrayList<Int>()
        val visitedPoints = ArrayList<Position>()
        val currentBasin = ArrayList<Position>()

        var startPositionForThisBasin: Position? = getNextCrawlerStart(map, visitedPoints)
        while (startPositionForThisBasin != null) {
            currentBasin.clear()
            crawl(map, currentBasin, startPositionForThisBasin)

            visitedPoints.addAll(currentBasin)
            sizes.add(currentBasin.size)
            startPositionForThisBasin = getNextCrawlerStart(map, visitedPoints)
        }
        println(sizes.sortedDescending().take(3).fold(1) { acc: Int, i: Int -> acc * i })
    }

    private fun crawl(
        map: Array<Array<Int>>,
        currentBasin: ArrayList<Position>,
        position: Position
    ) {
        position.crawled = true

        // Left
        if (map.getOrNull(position.y)?.getOrNull(position.x - 1).let { it != null && it != 9 }) {
            val nextPosition = currentBasin.find {
                it.x == position.x - 1 && it.y == position.y
            } ?: Position(position.x - 1, position.y).also {
                currentBasin.add(it)
            }

            if (!nextPosition.crawled) {
                crawl(map, currentBasin, nextPosition)
            }
        }

        // Top
        if (map.getOrNull(position.y - 1)?.getOrNull(position.x).let { it != null && it != 9 }) {
            val nextPosition = currentBasin.find {
                it.x == position.x && it.y == position.y - 1
            } ?: Position(position.x, position.y - 1).also {
                currentBasin.add(it)
            }

            if (!nextPosition.crawled) {
                crawl(map, currentBasin, nextPosition)
            }
        }

        // Right
        if (map.getOrNull(position.y)?.getOrNull(position.x + 1).let { it != null && it != 9 }) {
            val nextPosition = currentBasin.find {
                it.x == position.x + 1 && it.y == position.y
            } ?: Position(position.x + 1, position.y).also {
                currentBasin.add(it)
            }

            if (!nextPosition.crawled) {
                crawl(map, currentBasin, nextPosition)
            }
        }

        // Bottom
        if (map.getOrNull(position.y + 1)?.getOrNull(position.x).let { it != null && it != 9 }) {
            val nextPosition = currentBasin.find {
                it.x == position.x && it.y == position.y + 1
            } ?: Position(position.x, position.y + 1).also {
                currentBasin.add(it)
            }

            if (!nextPosition.crawled) {
                crawl(map, currentBasin, nextPosition)
            }
        }
    }

    private fun getNextCrawlerStart(map: Array<Array<Int>>, visitedPoints: ArrayList<Position>): Position? {
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                if (value != 9 && visitedPoints.find { it.x == x && it.y == y } == null) {
                    return Position(x, y)
                }
            }
        }

        return null
    }

    private data class Position(
        val x: Int,
        val y: Int,
        var crawled: Boolean = false
    )
}