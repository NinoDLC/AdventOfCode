package year2021

import utils.logMeasureTime
import java.io.File

class Day05 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day05.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day05().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day05().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        // X, Y, count
        val points = mutableListOf<PointAndCount>()

        lines.forEach { line ->
            val aAndB = line.split(" -> ")

            val xA = aAndB[0].split(",").first().toInt()
            val yA = aAndB[0].split(",")[1].toInt()
            val xB = aAndB[1].split(",").first().toInt()
            val yB = aAndB[1].split(",")[1].toInt()

            if (xA == xB || yA == yB) {
                for (i in xA.coerceAtMost(xB)..xA.coerceAtLeast(xB)) {
                    for (j in yA.coerceAtMost(yB)..yA.coerceAtLeast(yB)) {
                        addPoint(points, i, j)
                    }
                }
            }
        }

        var dangerLevelHigherThan2 = 0

        points.forEach {
            if (it.count >= 2) {
                dangerLevelHigherThan2++
            }
        }

        println(dangerLevelHigherThan2)
    }

    private fun partTwo(lines: List<String>) {
        // X, Y, count
        val points = mutableListOf<PointAndCount>()

        lines.forEach { line ->
            val aAndB = line.split(" -> ")

            val xA = aAndB[0].split(",").first().toInt()
            val yA = aAndB[0].split(",")[1].toInt()
            val xB = aAndB[1].split(",").first().toInt()
            val yB = aAndB[1].split(",")[1].toInt()

            val iterateX = when {
                xA == xB -> 0
                xA < xB -> 1
                else -> -1
            }
            val iterateY = when {
                yA == yB -> 0
                yA < yB -> 1
                else -> -1
            }

            var currentX = xA
            var currentY = yA

            while (currentX != xB || currentY != yB) {
                addPoint(points, currentX, currentY)

                currentX += iterateX
                currentY += iterateY
            }

            addPoint(points, currentX, currentY)
        }

        var dangerLevelHigherThan2 = 0

        points.forEach {
            if (it.count >= 2) {
                dangerLevelHigherThan2++
            }
        }

        println(dangerLevelHigherThan2)
    }

    private fun addPoint(
        points: MutableList<PointAndCount>,
        currentX: Int,
        currentY: Int
    ) {
        val existing = points.find {
            it.x == currentX && it.y == currentY
        }

        if (existing == null) {
            points.add(PointAndCount(currentX, currentY, 1))
        } else {
            existing.count++
        }
    }

    private fun printPoints(points: List<PointAndCount>) {
        val stringBuilder = StringBuilder()

        val jMax = points.maxOf { it.y }
        val iMax = points.maxOf { it.x }

        for (j in 0..jMax) {
            for (i in 0..iMax) {
                val existing = points.find {
                    it.x == i && it.y == j
                }

                stringBuilder.append(
                    existing?.count ?: "."
                )
            }
            stringBuilder.append("\n")
        }
        println(stringBuilder.toString())
    }

    private data class PointAndCount(
        val x: Int,
        val y: Int,
        var count: Int
    )
}