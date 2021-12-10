package year2015

import utils.logMeasureTime

class Day25 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("=== Part One ===")
            logMeasureTime {
                partOne()
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                partTwo()
            }
            println()
        }

        private fun partOne() {
            val map = Array<Array<Long>>(10_000) {
                Array(10_000) {
                    0
                }
            }

            map[1][1] = 20151125

            var xMax = 2
            while (true) {
                var y = 1
                for (x in xMax downTo 1) {
                    val previousNumber = if (y == 1) {
                        map[1][x - 1]
                    } else {
                        map[x + 1][y - 1]
                    }

                    map[x][y] = (previousNumber * 252533) % 33554393

                    if (x == 2981 && y == 3075) {
                        println(map[x][y])
                        return
                    }

                    y++
                }

                xMax++
            }
        }

        private fun partTwo() {
            println()
        }
    }
}