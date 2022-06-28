package utils

import kotlin.system.measureTimeMillis

fun <T> logMeasureTime(block: () -> T) : T {
    val result: T

    measureTimeMillis {
        result = block.invoke()
    }.let {
        println(
            if (it < 1_000) {
                "Took ${it}ms to run"
            } else {
                "Took ${it / 1_000}s to run"
            }
        )
    }

    return result
}