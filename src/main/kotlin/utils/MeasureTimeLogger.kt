package utils

import kotlin.system.measureTimeMillis

fun logMeasureTime(block: () -> Unit) {
    measureTimeMillis {
        block.invoke()
    }.let {
        println(
            if (it < 1_000) {
                "Took ${it}ms to run"
            } else {
                "Took ${it / 1_000}s to run"
            }
        )
    }
}