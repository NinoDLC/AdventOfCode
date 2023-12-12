package utils

import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

inline fun logMeasureTime(block: () -> Unit) {
    measureTimeMillis {
        block.invoke()
    }.milliseconds.let {
        println("Took $it to run")
    }
}