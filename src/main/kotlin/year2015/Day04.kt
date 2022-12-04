package year2015

import utils.logMeasureTime
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

class Day04 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("=== Part One ===")
            logMeasureTime {
                Day04().partOne()
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day04().partTwo()
            }
            println()
        }
    }

    private fun partOne() {
        for (i in 0..Int.MAX_VALUE) {
            val hashed = md5("iwrupvqb$i")
            val hex = hashed.toHex()
            if (hex.startsWith("00000")) {
                println(i)
                println(hex)
                println(hashed.joinToString())
                break
            }
        }
    }

    private fun partTwo() {
        val byte0: Byte = 0x0
        for (i in 0..Int.MAX_VALUE) {
            val hashed = md5("iwrupvqb$i")
            if (hashed[0] == byte0 && hashed[1] == byte0 && hashed[2] == byte0) {
                println(i)
                break
            }
        }
    }

    private fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
    private fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

}