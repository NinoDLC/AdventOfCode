@file:Suppress("unused")

package utils

data class Position(
    val x: Int,
    val y: Int,
) {
    fun toUp() = copy(y = y - 1)
    fun toUpRight() = copy(x = x + 1, y = y - 1)
    fun toRight() = copy(x = x + 1)
    fun toDownRight() = copy(x = x + 1, y = y + 1)
    fun toDown() = copy(y = y + 1)
    fun toDownLeft() = copy(x = x - 1, y = y + 1)
    fun toLeft() = copy(x = x - 1)
    fun toUpLeft() = copy(x = x - 1, y = y - 1)

    fun isToTheUpOf(other: Position) = y < other.y
    fun isToTheUpRightOf(other: Position) = x > other.x && y < other.y
    fun isToTheRightOf(other: Position) = x > other.x
    fun isToTheDownRightOf(other: Position) = x > other.x && y > other.y
    fun isToTheDownOf(other: Position) = y > other.y
    fun isToTheDownLeftOf(other: Position) = x < other.x && y > other.y
    fun isToTheLeftOf(other: Position) = x < other.x
    fun isToTheUpLeftOf(other: Position) = x < other.x && y < other.y
}