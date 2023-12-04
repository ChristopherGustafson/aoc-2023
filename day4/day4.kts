import java.io.File
import kotlin.math.pow

fun part1(input: String) =
    input.parseInput()
        .map { (winning, actual) -> points(winning, actual) }
        .sumOf { it }

fun String.parseInput() = this.split("\n")
    .filter { it.isNotBlank() }
    .map { it.split(": ").last() }
    .map { it.split(" | ").let { Pair(it.first().parseNumbers(), it.last().parseNumbers()) } }

fun String.parseNumbers() = this.split(" ", "  ").filter { it.isNotBlank() }

fun points(winning: List<String>, actual: List<String>) = 2.0.pow(matchingNumbers(winning, actual) - 1.0).toInt()

fun matchingNumbers(winning: List<String>, actual: List<String>): Int {
    val winningMap = winning.associateBy { it }
    return actual.filter { number -> winningMap.containsKey(number) }.size
}

fun part2(input: String): Int {
    val parsedInput = input.parseInput()
    val copies = parsedInput.indices.associateWith { 1 }.toMutableMap()
    val matchingNumbers = parsedInput.mapIndexed { index, (winning, actual) ->
        index to matchingNumbers(winning, actual)
    }.toMap()

    var totalCopies = 0
    parsedInput.indices.forEach { index ->
        val matching = matchingNumbers[index] ?: 0
        totalCopies += copies[index]!!
        for (i in index + 1..index + matching) { copies[i]?.let { copies[i] = it + copies[index]!! } }
    }
    return totalCopies
}

val exampleInput = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53\n" +
    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19\n" +
    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1\n" +
    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83\n" +
    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36\n" +
    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"

val realInput = File("day4_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
