import java.io.File

data class Map(val destinationStart: Long, val sourceStart: Long, val rangeLength: Long) {
    val sourceRange: LongRange get() = LongRange(sourceStart, sourceStart + rangeLength - 1)
}

fun List<Map>.mapNumber(number: Long): Long {
    for (map in this) {
        if (map.sourceRange.contains(number)) return map.destinationStart + (number - map.sourceStart)
    }
    return number
}

fun lowestLocationNumberFromSeeds(seeds: List<Long>, stages: List<List<Map>>): Long {
    var lowestLocationNumber = Long.MAX_VALUE
    for (seed in seeds) {
        val locationNumber = locationNumber(seed, stages)
        if (locationNumber < lowestLocationNumber) lowestLocationNumber = locationNumber
    }
    return lowestLocationNumber
}

fun lowestLocationNumberFromSeedNumbers(seedRanges: List<LongRange>, stages: List<List<Map>>): Long {
    var lowestLocationNumber = Long.MAX_VALUE
    for (seedRange in seedRanges) {
        for (seed in seedRange) {
            val locationNumber = locationNumber(seed, stages)
            if (locationNumber < lowestLocationNumber) lowestLocationNumber = locationNumber
        }
    }
    return lowestLocationNumber
}

fun locationNumber(seed: Long, stages: List<List<Map>>): Long {
    var currentNumber = seed
    for (stage in stages) {
        currentNumber = stage.mapNumber(currentNumber)
    }
    return currentNumber
}

fun parseStages(lines: List<String>) = lines.drop(1).map {
    it.split("\n").filter { it.isNotBlank() }.drop(1).map { line ->
        val (destinationStart, sourceStart, rangeLength) = line.split(" ").map { it.toLong() }
        Map(destinationStart, sourceStart, rangeLength)
    }
}

fun part1(input: String): Long {
    val lines = input.split("\n\n")
        .filter { it.isNotBlank() }
    val seeds = lines.first().split(" ").drop(1).map { it.toLong() }
    val stages = parseStages(lines)

    return lowestLocationNumberFromSeeds(seeds, stages)
}

fun part2(input: String): Long {
    val lines = input.split("\n\n")
        .filter { it.isNotBlank() }
    val seedRanges = lines.first().split(" ").drop(1).map { it.toLong() }
        .chunked(2)
        .map { (start, range) -> LongRange(start, start + range - 1) }

    val stages = parseStages(lines)
    return lowestLocationNumberFromSeedNumbers(seedRanges, stages)
}

val exampleInput = "seeds: 79 14 55 13\n" +
    "\n" +
    "seed-to-soil map:\n" +
    "50 98 2\n" +
    "52 50 48\n" +
    "\n" +
    "soil-to-fertilizer map:\n" +
    "0 15 37\n" +
    "37 52 2\n" +
    "39 0 15\n" +
    "\n" +
    "fertilizer-to-water map:\n" +
    "49 53 8\n" +
    "0 11 42\n" +
    "42 0 7\n" +
    "57 7 4\n" +
    "\n" +
    "water-to-light map:\n" +
    "88 18 7\n" +
    "18 25 70\n" +
    "\n" +
    "light-to-temperature map:\n" +
    "45 77 23\n" +
    "81 45 19\n" +
    "68 64 13\n" +
    "\n" +
    "temperature-to-humidity map:\n" +
    "0 69 1\n" +
    "1 0 69\n" +
    "\n" +
    "humidity-to-location map:\n" +
    "60 56 37\n" +
    "56 93 4"

val realInput = File("day5_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
