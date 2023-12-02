import java.io.File

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { line -> line.split((": ")).toPair() }
        .fold(0) { total, (id, game) -> if (game.isValidGame()) total + id.toIdNumber() else total }

fun String.toIdNumber() = this.substring(5).toInt()

fun String.isValidGame() =
    this.split(", ", "; ")
        .none { it.isNotValidGrab() }

val cubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun String.isNotValidGrab() =
    this.split(" ").toPair().let { (amount, color) -> amount.toInt() > cubes[color]!! }

fun List<String>.toPair() = this.zipWithNext().single()

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { line -> line.split((": ")).last() }
        .map { game -> game.minimumSet() }
        .sumOf { minimumSet -> minimumSet.power() }

fun String.minimumSet() =
    this.split(", ", "; ")
        .map { grab -> grab.split(" ").toPair() }
        .map { Pair(it.first.toInt(), it.second) }
        .fold(mapOf("red" to 0, "green" to 0, "blue" to 0)) { set, (amount, color) ->
            set.plus(color to set[color]!!.coerceAtLeast(amount))
        }

fun Map<String, Int>.power() = this.values.reduce { a, b -> a * b }

val exampleInput = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n" +
    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n" +
    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n" +
    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n" +
    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"

val realInput = File("day2_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
