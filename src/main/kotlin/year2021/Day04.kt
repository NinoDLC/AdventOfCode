package year2021

import utils.logMeasureTime
import java.io.File

class Day04 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = File("data/2021/day04.txt").readLines()

            println("=== Part One ===")
            logMeasureTime {
                Day04().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day04().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val winningNumbers = lines.first().split(",")

        val boards = getAllBoards(lines)

        winningNumbers.map { it.toInt() }.forEach { winningNumber ->
            boards.forEach { board ->
                board.forEach { boardLine ->
                    boardLine.forEachIndexed { index, boardNumber ->
                        if (boardNumber.first == winningNumber) {
                            boardLine[index] = boardNumber.copy(
                                second = true
                            )
                        }
                    }
                }

                val sumOfWinningBoard = getSumOfWinningBoard(board)

                if (sumOfWinningBoard != null) {
                    println(sumOfWinningBoard * winningNumber)
                    return
                }
            }
        }

        println("No winning board found :(")
    }

    private fun getAllBoards(lines: List<String>): List<Array<Array<Pair<Int, Boolean>>>> {
        val boards = ArrayList<Array<Array<Pair<Int, Boolean>>>>()

        var currentBoard: Array<Array<Pair<Int, Boolean>>> = getFreshBoard()
        var indexInCurrentBoard = 0

        lines.drop(2).forEach { line ->
            if (line.isEmpty()) {
                boards.add(currentBoard)
                indexInCurrentBoard = 0
                currentBoard = getFreshBoard()
            } else {
                val numbers = line.trim().split(" ").filter { it.isNotBlank() }
                numbers.onEachIndexed { index, number ->
                    currentBoard[indexInCurrentBoard][index] = Pair(number.toInt(), false)
                }

                indexInCurrentBoard++
            }
        }

        boards.add(currentBoard)
        return boards
    }

    private fun getFreshBoard() = Array(5) {
        Array(5) { Pair(0, false) }
    }

    private fun getSumOfWinningBoard(board: Array<Array<Pair<Int, Boolean>>>): Int? {
        for (i in 0..4) {
            var allWinningHorizontally = true

            for (j in 0..4) {
                if (!board[i][j].second) {
                    allWinningHorizontally = false
                }

                if (!allWinningHorizontally) {
                    break
                }
            }

            if (allWinningHorizontally) {
                return getSumOfUnmarkedNumbers(board)
            }
        }

        for (i in 0..4) {
            var allWinningVertically = true

            for (j in 0..4) {

                if (!board[j][i].second) {
                    allWinningVertically = false
                }

                if (!allWinningVertically) {
                    break
                }
            }

            if (allWinningVertically) {
                return getSumOfUnmarkedNumbers(board)
            }
        }

        return null
    }

    private fun getSumOfUnmarkedNumbers(board: Array<Array<Pair<Int, Boolean>>>): Int {
        return board.sumOf { line ->
            line.sumOf {
                if (!it.second) {
                    it.first
                } else {
                    0
                }
            }
        }
    }

    private fun partTwo(lines: List<String>) {
        val winningNumbers = lines.first().split(",")

        var boards = getAllBoards(lines)
        var lastWinningBoardScore: Int? = null

        winningNumbers.map { it.toInt() }.forEach { winningNumber ->
            boards = boards.filter { board ->
                board.forEach { boardLine ->
                    boardLine.forEachIndexed { index, boardNumber ->
                        if (boardNumber.first == winningNumber) {
                            boardLine[index] = boardNumber.copy(
                                second = true
                            )
                        }
                    }
                }

                val sumOfWinningBoard = getSumOfWinningBoard(board)

                if (sumOfWinningBoard != null) {
                    lastWinningBoardScore = sumOfWinningBoard * winningNumber
                    false
                } else {
                    true
                }
            }
        }

        println(lastWinningBoardScore)
    }
}