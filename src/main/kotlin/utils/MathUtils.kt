package utils

fun Long.lcm(other: Long): Long = (this * other) / this.gcd(other)

tailrec fun Long.gcd(other: Long): Long =
    if (other == 0L) {
        this
    } else {
        other.gcd(this % other)
    }