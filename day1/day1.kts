package day1

import java.io.File

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { line -> "${line.first { it.isDigit() }}${line.last { it.isDigit() }}" }
        .sumOf { it.toInt() }

val numbers = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { replaceSpelledNumber(it, CharSequence::findAnyOf) }
        .map { replaceSpelledNumber(it, CharSequence::findLastAnyOf) }
        .map { line -> "${line.first { it.isDigit() }}${line.last { it.isDigit() }}" }
        .sumOf { it.toInt() }

fun replaceSpelledNumber(line: String, find: CharSequence.(Collection<String>) -> Pair<Int, String>?) =
    line.find(numbers.keys)?.let { (index, string) ->
        line.replaceRange(index, index + string.length, numbers[string].toString())
    } ?: line

val exampleInput = "1abc2\npqr3stu8vwx\na1b2c3d4e5f\ntreb7uchet"
println("Example part 1: " + part1(exampleInput))

val realInput = File("day1_input.txt").readText()
println("Real input part 1: " + part1(realInput))

val exampleInput2 = "two1nine\neightwothree\nabcone2threexyz\nxtwone3four\n4nineeightseven2\nzoneight234\n7pqrstsixteen"
println("Example part 2: " + part2(exampleInput2))

println("Real input part 2: " + part2(realInput))
