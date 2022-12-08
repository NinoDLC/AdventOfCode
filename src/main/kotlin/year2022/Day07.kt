package year2022

import utils.getPuzzleInput
import utils.logMeasureTime

class Day07 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day07().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day07().partTwo(lines)
            }
            println()
        }

        private val numbersAtStartOfStringRegex = Regex("^(\\d+) .*")
    }

    private fun partOne(lines: List<String>) {
        val master = computeFileTree(lines)

        println(
            getFileSizesOfAtMostSize(master, 100_000).sum()
        )
    }

    private fun partTwo(lines: List<String>) {
        val master = computeFileTree(lines)
        val sizes = getFileSizesOfAtMostSize(master, Int.MAX_VALUE)
        val availableSize = 70_000_000 - sizes.max()
        val neededSize = 30_000_000 - availableSize

        printFiles(master)

        println(
            sizes.sorted().find {
                it > neededSize
            }
        )
    }

    private fun computeFileTree(lines: List<String>): File {
        val master = File("", 0, null, mutableListOf())
        val currentPath = mutableListOf<String>()

        lines.forEach { line ->
            when {
                line.startsWith("\$ cd") -> {
                    val directoryName = line.substring(5)

                    if (directoryName == "..") {
                        currentPath.removeLast()
                    } else {
                        currentPath.add(directoryName)
                        get(master = master, path = currentPath, createAllowed = true)
                    }
                }

                line.matches(numbersAtStartOfStringRegex) -> {
                    val size = numbersAtStartOfStringRegex.find(line)!!.groupValues[1].toInt()
                    updateSize(get(master = master, path = currentPath), size)
                }
            }
        }

        return master
    }

    private fun updateSize(file: File, size: Int) {
        var currentFile: File? = file

        while (currentFile != null) {
            currentFile.size += size
            currentFile = currentFile.parent
        }
    }

    private fun get(master: File, path: List<String>, createAllowed: Boolean = false): File =
        path.foldIndexed(master) { index: Int, acc: File, currentDirectoryName: String ->
            acc.children.find { it.name == currentDirectoryName } ?: if (createAllowed && index == path.size - 1) {
                File(name = path[index], size = 0, parent = acc, children = mutableListOf()).also { created ->
                    acc.children.add(created)
                }
            } else {
                throw IllegalStateException(
                    "Can't find directory ${path.joinToString(separator = "/")} (index = $index, createAllowed = $createAllowed) in $master"
                )
            }
        }

    private fun getFileSizesOfAtMostSize(file: File, maxSize: Int): List<Int> = file.children.map {
        getFileSizesOfAtMostSize(it, maxSize)
    }.flatten()
        .let { list ->
            if (file.size < maxSize) {
                list + file.size
            } else {
                list
            }
        }

    private fun printFiles(file: File) {
        var path = ""
        var currentFile: File? = file

        while (currentFile != null && currentFile.name.isNotEmpty()) {
            path = if (currentFile.name != "/") {
                "${currentFile.name}/$path"
            } else {
                "/$path"
            }
            currentFile = currentFile.parent
        }

        println("$path (size= ${file.size.toString().reversed().chunked(3).joinToString(separator = "_").reversed()} )")
        file.children.forEach { child ->
            printFiles(child)
        }
    }

    data class File(
        val name: String,
        var size: Int,
        val parent: File?,
        val children: MutableList<File>,
    )
}