import java.io.File
import kotlin.math.abs

fun distance(
    a: Pair<Int, Int>,
    b: Pair<Int, Int>,
    rowWithoutGalaxy: Map<Int, Boolean>,
    colWithoutGalaxy: Map<Int, Boolean>,
    expansion: Long
): Long {
    val rowDistance = abs(a.second - b.second)
    val colDistance = abs(a.first - b.first)

    // Rows
    var doubleRows = 0L
    if (rowDistance > 1) {
        val rowRange = if (a.second < b.second) IntRange(a.second + 1, b.second) else IntRange(b.second + 1, a.second)
        for (i in rowRange) if (rowWithoutGalaxy[i] == true) doubleRows += expansion - 1L
    }
    // Cols
    var doubleCols = 0L
    if (colDistance > 1) {
        val colRange = if (a.first < b.first) IntRange(a.first + 1, b.first) else IntRange(b.first + 1, a.first)
        for (i in colRange) if (colWithoutGalaxy[i] == true) doubleCols += expansion - 1L
    }
    return rowDistance + doubleRows + colDistance + doubleCols
}

fun sumDistances(input: String, expansion: Long): Long {
    val parsedInput = input.split("\n")
        .filter { it.isNotBlank() }
        .map { it.toList() }

    val rowWithoutGalaxy = parsedInput.indices.associateWith { true }.toMutableMap()
    val colWithoutGalaxy = parsedInput.first().indices.associateWith { true }.toMutableMap()
    val galaxies = mutableListOf<Pair<Int, Int>>()

    parsedInput.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == '#') {
                galaxies.add(Pair(x, y))
                rowWithoutGalaxy[y] = false
                colWithoutGalaxy[x] = false
            }
        }
    }

    var sum = 0L
    for (a in galaxies.indices) {
        for (b in a + 1..<galaxies.size) {
            sum += distance(galaxies[a], galaxies[b], rowWithoutGalaxy, colWithoutGalaxy, expansion)
        }
    }
    return sum
}


fun part1(input: String) = sumDistances(input, 2L)
fun part2(input: String) = sumDistances(input, 1000000L)

val exampleInput = "...#......\n" +
    ".......#..\n" +
    "#.........\n" +
    "..........\n" +
    "......#...\n" +
    ".#........\n" +
    ".........#\n" +
    "..........\n" +
    ".......#..\n" +
    "#...#....."

val realInput = File("day11_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
