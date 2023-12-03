import java.io.File

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .let { line -> Pair(line.first().length, line.flatMap { it.toList() }.associateByIndex()) }
        .let { (lineLength, schema) -> sumPartNumbers(schema, lineLength) }

fun List<Char>.associateByIndex() = this.withIndex().associateBy({ it.index }, { it.value })

fun sumPartNumbers(schema: Map<Int, Char>, lineLength: Int): Int {
    var currentIndex = 0
    var sum = 0
    while (currentIndex < schema.size) {
        if (schema[currentIndex].isDigitNullable() && hasAdjacentSymbol(schema, currentIndex, lineLength)) {
            val (number, numberEndIndex) = schema.getNumberAndEndIndexAt(currentIndex)
            sum += number
            currentIndex = numberEndIndex + 1
        } else {
            currentIndex++
        }
    }
    return sum
}

fun Map<Int, Char>.getNumberAndEndIndexAt(index: Int): Pair<Int, Int> {
    var number = this[index].toString()
    var numberStartIndex = index - 1
    while (this[numberStartIndex].isDigitNullable()) number =
        this[numberStartIndex]!!.plus(number).also { numberStartIndex-- }
    var numberEndIndex = index + 1
    while (this[numberEndIndex].isDigitNullable()) number =
        number.plus(this[numberEndIndex]!!).also { numberEndIndex++ }
    return Pair(number.toInt(), numberEndIndex)
}

fun Char?.isDigitNullable(): Boolean = this?.isDigit() ?: false

fun hasAdjacentSymbol(schema: Map<Int, Char>, currentIndex: Int, lineLength: Int): Boolean {
    for (i in currentIndex - 1..currentIndex + 1) {
        if (schema[i - lineLength].isSymbol() || schema[i].isSymbol() || schema[i + lineLength].isSymbol()) return true
    }
    return false
}

fun adjacentNumbers(schema: Map<Int, Char>, currentIndex: Int, lineLength: Int): Set<Int> {
    val adjacentNumbers = mutableSetOf<Int>()
    for (i in currentIndex - 1..currentIndex + 1) {
        if (schema[i - lineLength].isDigitNullable()) adjacentNumbers.add(schema.getNumberAndEndIndexAt(i - lineLength).first)
        if (schema[i].isDigitNullable()) adjacentNumbers.add(schema.getNumberAndEndIndexAt(i).first)
        if (schema[i + lineLength].isDigitNullable()) adjacentNumbers.add(schema.getNumberAndEndIndexAt(i + lineLength).first)
    }
    return adjacentNumbers
}

fun Char?.isSymbol() = this?.let { it != '.' && !it.isDigit() } ?: false

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .let { line -> Pair(line.first().length, line.flatMap { it.toList() }.associateByIndex()) }
        .let { (lineLength, schema) -> sumGearRatios(schema, lineLength) }

fun sumGearRatios(schema: Map<Int, Char>, lineLength: Int): Int {
    var currentIndex = 0
    var sum = 0
    while (currentIndex < schema.size) {
        if (schema[currentIndex].isGear()) {
            val adjacentNumbers = adjacentNumbers(schema, currentIndex, lineLength)
            if (adjacentNumbers.size == 2) sum += adjacentNumbers.reduce { a, b -> a * b }
        }
        currentIndex++
    }
    return sum
}

fun Char?.isGear() = this == '*'

val exampleInput = "467..114..\n" +
    "...*......\n" +
    "..35..633.\n" +
    "......#...\n" +
    "617*......\n" +
    ".....+.58.\n" +
    "..592.....\n" +
    "......755.\n" +
    "...$.*....\n" +
    ".664.598.."

val realInput = File("day3_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
