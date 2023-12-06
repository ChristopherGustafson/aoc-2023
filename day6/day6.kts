import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

/*
 * d = distance
 * t = time
 * a = acceleration
 * w = waitingTime
 *
 * d < (w * a) * (t - w)
 * w^2 - tw + d / a < 0
 * Using the quadratic equation:
 * w < -(-t/2) +- sqrt((t/2)^2 - d/a)
 * x = -(-t/2)
 * y = sqrt((t/2)^2 - d/a)
 *
 *  => x - y < w < x + y
 */

val acceleration = 1.0

data class Race(val time: Double, val distance: Double)

fun pq(d: Double, t: Double, a: Double): Int {
    val x = t / 2.0
    val y = sqrt((t / 2).pow(2) - (d / a))

    // Return number of ints in the span (x+y, x-y)
    return (ceil(x + y) - floor(x - y) - 1.0).toInt()
}

fun String.parseLine() = this.split("\\s+".toRegex()).drop(1).map { it.toDouble() }

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .let { (times, distances) -> times.parseLine().zip(distances.parseLine()).map { Race(it.first, it.second) } }
        .fold(1) { p, r -> p * pq(r.distance, r.time, acceleration) }

fun String.parseLineToSingleValue() = this.split("\\s+".toRegex()).drop(1).joinToString("").toDouble()

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .let { (times, distances) -> Race(times.parseLineToSingleValue(), distances.parseLineToSingleValue()) }
        .let { r -> pq(r.distance, r.time, acceleration) }


val exampleInput = "Time:      7  15   30\n" +
    "Distance:  9  40  200\n"

val realInput = File("day6_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
