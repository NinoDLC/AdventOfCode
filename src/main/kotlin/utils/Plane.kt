package utils

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Plane<T : Any>(
    val xMax: Int,
    val yMax: Int,
    init: (x: Int, y: Int) -> T,
) {

    companion object {
        fun <T : Any> of(lines: List<List<T>>): Plane<T> = Plane(
            xMax = lines.maxOf { it.size },
            yMax = lines.size,
        ) { x, y ->
            lines[y][x]
        }

        @JvmName("ofStrings")
        fun of(lines: List<String>): Plane<Char> = Plane(
            xMax = lines.maxOf { it.length },
            yMax = lines.size,
        ) { x, y ->
            lines[y][x]
        }

        /**
         * Sum the value from all range-matching items.
         */
        fun Plane<out Number>.sum(
            xProgression: IntProgression = 0 until xMax,
            yProgression: IntProgression = 0 until yMax,
        ): Long = sumOf(xProgression, yProgression) { (item) ->
            item.toLong()
        }
    }

    private val rows: Array<Array<Any?>> = Array(xMax) { x ->
        Array(yMax) { y ->
            init(x, y)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun get(x: Int, y: Int): ItemPosition<T> = ItemPosition(
        rows[x][y] as T,
        Position(x, y),
    )

    fun get(position: Position): ItemPosition<T> = get(position.x, position.y)

    fun upOf(item: T): ItemPosition<T>? = positionOfOrNull { existing -> existing == item }?.let { upOf(it.position) }

    fun upOf(itemPosition: ItemPosition<T>): ItemPosition<T>? = upOf(itemPosition.position)

    fun upOf(position: Position): ItemPosition<T>? = if (position.y - 1 >= 0) {
        get(Position(x = position.x, y = position.y - 1))
    } else {
        null
    }

    fun rightOf(item: T): ItemPosition<T>? = positionOfOrNull { existing -> existing == item }?.let { rightOf(it.position) }

    fun rightOf(itemPosition: ItemPosition<T>): ItemPosition<T>? = rightOf(itemPosition.position)

    fun rightOf(position: Position): ItemPosition<T>? = if (position.x + 1 < xMax) {
        get(Position(x = position.x + 1, y = position.y))
    } else {
        null
    }

    fun downOf(item: T): ItemPosition<T>? = positionOfOrNull { existing -> existing == item }?.let { downOf(it.position) }

    fun downOf(itemPosition: ItemPosition<T>): ItemPosition<T>? = downOf(itemPosition.position)

    fun downOf(position: Position): ItemPosition<T>? = if (position.y + 1 < yMax) {
        get(Position(x = position.x, y = position.y + 1))
    } else {
        null
    }

    fun leftOf(item: T): ItemPosition<T>? = positionOfOrNull { existing -> existing == item }?.let { leftOf(it.position) }

    fun leftOf(itemPosition: ItemPosition<T>): ItemPosition<T>? = leftOf(itemPosition.position)

    fun leftOf(position: Position): ItemPosition<T>? = if (position.x - 1 >= 0) {
        get(Position(x = position.x - 1, y = position.y))
    } else {
        null
    }

    fun positionOfOrNull(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): ItemPosition<T>? {
        forEach(xProgression, yProgression) { item ->
            if (predicate(item)) {
                return item
            }
        }

        return null
    }

    fun positionOf(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): ItemPosition<T> = positionOfOrNull(xProgression, yProgression, predicate) ?: throw IllegalStateException(
        "Couldn't find the matching item's position in xProgression = $xProgression and yProgression = $yProgression"
    )

    /**
     * Transforms an item at said position.
     */
    fun transform(x: Int, y: Int, lambda: (itemPosition: ItemPosition<T>) -> T) {
        rows[x][y] = lambda(get(x, y))
    }

    /**
     * Transforms all range-matching items, in the reading order (left to right, then on end of line to the next line, etc...)
     */
    fun transform(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        lambda: (itemPosition: ItemPosition<T>) -> T,
    ) {
        forEach(xProgression, yProgression) { item ->
            rows[item.position.x][item.position.y] = lambda(item)
        }
    }

    /**
     * Accumulate a value from all range-matching items.
     */
    fun <R> fold(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        initialValue: R,
        operation: (acc: R, itemPosition: ItemPosition<T>) -> R,
    ): R {
        var accumulator = initialValue
        forEach(xProgression, yProgression) { item ->
            accumulator = operation(accumulator, item)
        }
        return accumulator
    }

    /**
     * Sum the value from all range-matching items.
     */
    fun sumOf(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        operation: (itemPosition: ItemPosition<T>) -> Long,
    ): Long = fold(xProgression, yProgression, 0) { acc: Long, itemPosition: ItemPosition<T> ->
        acc + operation(itemPosition)
    }

    fun all(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): Boolean {
        forEach(xProgression, yProgression) { item ->
            if (!predicate(item)) {
                return false
            }
        }

        return true
    }

    fun any(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): Boolean {
        forEach(xProgression, yProgression) { item ->
            if (predicate(item)) {
                return true
            }
        }

        return false
    }

    fun none(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): Boolean {
        forEach(xProgression, yProgression) { item ->
            if (predicate(item)) {
                return false
            }
        }

        return true
    }

    fun filter(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        predicate: (itemPosition: ItemPosition<T>) -> Boolean,
    ): List<ItemPosition<T>> {
        val destination = mutableListOf<ItemPosition<T>>()

        forEach(xProgression, yProgression) { item ->
            if (predicate(item)) {
                destination.add(item)
            }
        }

        return destination
    }

    inline fun forEach(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        action: (itemPosition: ItemPosition<T>) -> Unit,
    ) {
        yProgression.forEach { y ->
            xProgression.forEach { x ->
                action(get(x, y))
            }
        }
    }

    fun forEachColumn(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        action: (column: Column<T>) -> Unit,
    ) {
        // x and y loops are inverted to group by columns
        xProgression.forEach { x ->
            val itemsInColumn = mutableListOf<ItemPosition<T>>()
            yProgression.forEach { y ->
                itemsInColumn.add(get(x, y))
            }

            action(
                Column(
                    items = itemsInColumn,
                    x = x,
                )
            )
        }
    }

    fun forEachRow(
        xProgression: IntProgression = 0 until xMax,
        yProgression: IntProgression = 0 until yMax,
        action: (row: Row<T>) -> Unit,
    ) {
        yProgression.forEach { y ->
            val itemsInRow = mutableListOf<ItemPosition<T>>()
            xProgression.forEach { x ->
                itemsInRow.add(get(x, y))
            }

            action(
                Row(
                    items = itemsInRow,
                    y = y,
                )
            )
        }
    }

    override fun toString(): String = joinToString()

    fun joinToString(
        xWindow: IntProgression = 0 until xMax,
        yWindow: IntProgression = 0 until yMax,
        withPrettify: Boolean = true,
        extraPadding: Int = 0,
        itemTransform: (itemPosition: ItemPosition<T>) -> String = { (item) -> item.toString() },
    ): String = joinToString(
        xWindow = xWindow,
        yWindow = yWindow,
        withColumnsIndicator = withPrettify,
        withRowsIndicator = withPrettify,
        withSeparators = withPrettify,
        extraPadding = extraPadding,
        itemTransform = itemTransform,
    )

    fun joinToString(
        xWindow: IntProgression = 0 until xMax,
        yWindow: IntProgression = 0 until yMax,
        withColumnsIndicator: Boolean = true,
        withRowsIndicator: Boolean = true,
        withSeparators: Boolean = true,
        extraPadding: Int = 0,
        itemTransform: (itemPosition: ItemPosition<T>) -> String = { (item) -> item.toString() },
    ): String {
        require(
            xWindow.step >= 1 && xWindow.first >= 0 || xWindow.step <= 1 && xWindow.first < xMax
        ) { "Incorrect xProgression first bound: ${xWindow.first}" }
        require(
            xWindow.step >= 1 && xWindow.last < xMax || xWindow.step <= 1 && xWindow.last >= 0
        ) {
            "Incorrect xProgression last bound: ${xWindow.last}"
        }
        require(
            yWindow.step >= 1 && yWindow.first >= 0 || yWindow.step <= 1 && yWindow.first < yMax
        ) { "Incorrect yProgression first bound: ${yWindow.first}" }
        require(
            yWindow.step >= 1 && yWindow.last < yMax || yWindow.step <= 1 && yWindow.last >= 0
        ) {
            "Incorrect yProgression last bound: ${yWindow.last}"
        }

        // +2 for padding around row indicator numbers because it's prettier
        val rowsIndicatorSize = (yWindow.last - 1).toString().length + 2
        val longestItemStringLength = yWindow.maxOf { y ->
            xWindow.maxOf { x ->
                itemTransform(get(x, y)).length + extraPadding * 2
            }
        }
        val longestItemAndHeaderStringLength = if (withColumnsIndicator) {
            val longestColumnIndicatorSize = xWindow.last.toString().length
            longestItemStringLength.coerceAtLeast(longestColumnIndicatorSize + extraPadding * 2)
        } else {
            longestItemStringLength
        }

        val stringBuilder = StringBuilder()

        if (withColumnsIndicator) {
            // Line 1: Headers
            if (withRowsIndicator) {
                stringBuilder.append(" ".repeat(rowsIndicatorSize))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            xWindow.forEach { x ->
                stringBuilder.append(x.toString().center(longestItemAndHeaderStringLength))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            stringBuilder.appendLine()

            // Line 2: Separator
            if (withRowsIndicator) {
                stringBuilder.append("-".repeat(rowsIndicatorSize))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            repeat(xWindow.count()) {
                stringBuilder.append("-".repeat(longestItemAndHeaderStringLength))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            stringBuilder.appendLine()
        }

        // Actual values lines
        yWindow.forEach { y ->
            if (withRowsIndicator) {
                stringBuilder.append(y.toString().center(rowsIndicatorSize))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            xWindow.forEach { x ->
                stringBuilder.append(itemTransform(get(x, y)).center(longestItemAndHeaderStringLength))
                if (withSeparators) {
                    stringBuilder.append('|')
                }
            }
            stringBuilder.appendLine()
        }

        return stringBuilder.toString()
    }

    data class ItemPosition<T>(
        val item: T,
        val position: Position,
    )

    data class Column<T>(
        val items: List<ItemPosition<T>>,
        val x: Int,
    )

    data class Row<T>(
        val items: List<ItemPosition<T>>,
        val y: Int,
    )
}