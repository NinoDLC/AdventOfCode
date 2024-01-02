@file:Suppress("unused")

package utils

import kotlin.math.abs

data class Position(
    val x: Int,
    val y: Int,
) {
    fun toNorth(count: Int = 1) = copy(y = y - count)
    fun toNorthEast(count: Int = 1) = copy(x = x + count, y = y - count)
    fun toEast(count: Int = 1) = copy(x = x + count)
    fun toSouthEast(count: Int = 1) = copy(x = x + count, y = y + count)
    fun toSouth(count: Int = 1) = copy(y = y + count)
    fun toSouthWest(count: Int = 1) = copy(x = x - count, y = y + count)
    fun toWest(count: Int = 1) = copy(x = x - count)
    fun toNorthWest(count: Int = 1) = copy(x = x - count, y = y - count)

    val neighbours: List<Position>
        get() = listOf(
            toNorth(),
            toEast(),
            toSouth(),
            toWest(),
        )

    fun isToTheNorthOf(other: Position) = y < other.y
    fun isToTheNorthEastOf(other: Position) = x > other.x && y < other.y
    fun isToTheEastOf(other: Position) = x > other.x
    fun isToTheSouthEastOf(other: Position) = x > other.x && y > other.y
    fun isToTheSouthOf(other: Position) = y > other.y
    fun isToTheSouthWestOf(other: Position) = x < other.x && y > other.y
    fun isToTheWestOf(other: Position) = x < other.x
    fun isToTheNorthWestOf(other: Position) = x < other.x && y < other.y

    fun getDirectionTo(other: Position): Direction = when {
        x == other.x && y < other.y -> Direction.SOUTH
        x == other.x && y > other.y -> Direction.NORTH
        x < other.x && y == other.y -> Direction.EAST
        x > other.x && y == other.y -> Direction.WEST
        else -> throw IllegalArgumentException("Unknown direction between [$x,$y] and [${other.x},${other.y}]")
    }

    fun getDistanceTo(other: Position): Int = abs(x - other.x) + abs(y - other.y)

    override fun toString(): String = "[$x,$y]"
}