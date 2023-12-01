package utils

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Plane<T : Any>(
    private val xMax: Int,
    private val yMax: Int,
    init: (x: Int, y: Int) -> T,
) {
    private val rows: Array<Array<Any?>> = Array(xMax) { x ->
        Array(yMax) { y ->
            init(x, y)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun get(x: Int, y: Int): T = rows[x][y] as T

    /**
     * Transforms an item at said position.
     */
    fun transform(x: Int, y: Int, lambda: (existing: T, x: Int, y: Int) -> T) {
        rows[x][y] = lambda(get(x, y), x, y)
    }

    /**
     * Transforms all range-matching items at the same time.
     *
     * If every item change should be reflected after each point transformation instead, use [transformEach].
     */
    fun transform(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        lambda: (existing: T, x: Int, y: Int) -> T,
    ) {
        xProgression.forEach { x ->
            yProgression.forEach { y ->
                transform(x, y, lambda)
            }
        }
    }

    /**
     * Transforms range-matching items one by one, changing the value(s) with lowest x first, and in this subset, by y.
     * Then higher x, and so on.
     *
     * If every item change should be reflected at all once, use [transform] instead.
     */
    fun transformEach(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        lambda: (existing: T, x: Int, y: Int) -> T,
    ) {
        xProgression.forEach { x ->
            yProgression.forEach { y ->
                @Suppress("UNCHECKED_CAST")
                lambda(rows[x][y] as T, x, y)
            }
        }
    }

    /**
     * Accumulate a value from all range-matching items.
     */
    fun <R> fold(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        initialValue: R,
        operation: (acc: R, item: T, x: Int, y: Int) -> R
    ): R {
        var accumulator = initialValue
        xProgression.forEach { x ->
            yProgression.forEach { y ->
                @Suppress("UNCHECKED_CAST")
                accumulator = operation(accumulator, rows[x][y] as T, x, y)
            }
        }
        return accumulator
    }

    /**
     * Sum the value from all range-matching items.
     */
    fun sumOf(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        operation: (item: T, x: Int, y: Int) -> Long
    ): Long = fold(xProgression, yProgression, 0) { acc: Long, item: T, x: Int, y: Int ->
        acc + operation(item, x, y)
    }

    /**
     * Sum the value from all range-matching items.
     *
     * Unsafe: use only with a Plane containing [Number]s.
     */
    fun sum(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
    ): Long = fold(xProgression, yProgression, 0) { acc: Long, item: T, _, _ ->
        acc + (item as Number).toLong()
    }

    override fun toString(): String = joinToString()

    fun joinToString(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        extraPadding: Int = 0,
        itemTransform: (T) -> String = { it.toString() },
    ): String {
        require(
            xProgression.step >= 1 && xProgression.first >= 0 || xProgression.step <= 1 && xProgression.first < xMax
        ) { "Incorrect xProgression first bound: ${xProgression.first}" }
        require(
            xProgression.step >= 1 && xProgression.last < xMax || xProgression.step <= 1 && xProgression.last >= 0
        ) {
            "Incorrect xProgression last bound: ${xProgression.last}"
        }
        require(
            yProgression.step >= 1 && yProgression.first >= 0 || yProgression.step <= 1 && yProgression.first < yMax
        ) { "Incorrect yProgression first bound: ${yProgression.first}" }
        require(
            yProgression.step >= 1 && yProgression.last < yMax || yProgression.step <= 1 && yProgression.last >= 0
        ) {
            "Incorrect yProgression last bound: ${yProgression.last}"
        }

        val columnSize = (yProgression.last - 1).toString().length + 2
        val rowSize = (xProgression.last - 1).toString().length
        val highestItemStringSize = yProgression.maxOf { y ->
            xProgression.maxOf { x ->
                itemTransform(get(x, y)).length + extraPadding * 2
            }
        }.coerceAtLeast(rowSize + extraPadding * 2)

        val stringBuilder = StringBuilder()

        // Line 1: Headers
        stringBuilder.append(" ".repeat(columnSize))
        stringBuilder.append('|')
        xProgression.forEach { x ->
            stringBuilder.append(x.toString().center(highestItemStringSize))
            stringBuilder.append('|')
        }
        stringBuilder.appendLine()

        // Line 2: Separator
        stringBuilder.append("-".repeat(columnSize))
        stringBuilder.append('|')
        repeat(xProgression.count()) {
            stringBuilder.append("-".repeat(highestItemStringSize))
            stringBuilder.append('|')
        }
        stringBuilder.appendLine()

        // Actual values lines
        yProgression.forEach { y ->
            stringBuilder.append(y.toString().center(columnSize))
            stringBuilder.append('|')
            xProgression.forEach { x ->
                stringBuilder.append(itemTransform(get(x, y)).center(highestItemStringSize))
                stringBuilder.append('|')
            }
            stringBuilder.appendLine()
        }

        return stringBuilder.toString()
    }
}