import java.io.File
import java.util.Stack

fun extrapolatedValue(line: List<Int>): Int {
    val diffs = Stack<List<Int>>()
    diffs.push(line)
    var diff = line.zipWithNext { a, b -> b - a }
    while(diff.any { it != 0 }){
        diffs.push(diff)
        diff = diffs.peek().zipWithNext { a, b -> b - a }
    }
    var result = diffs.pop().last()
    while(diffs.isNotEmpty()){
        result += diffs.pop().last()
    }
    return result
}

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { line -> line.split(" ").map { it.toInt() } }
        .sumOf { extrapolatedValue(it) }

fun backwardsExtrapolatedValue(line: List<Int>): Int {
    val diffs = Stack<List<Int>>()
    diffs.push(line)
    var diff = line.zipWithNext { a, b -> b - a }
    while(diff.any { it != 0 }){
        diffs.push(diff)
        diff = diffs.peek().zipWithNext { a, b -> b - a }
    }
    var result = diffs.pop().first()
    while(diffs.isNotEmpty()){
        result = diffs.pop().first() - result
    }
    return result
}

fun part2(input: String) = input.split("\n")
    .filter { it.isNotBlank() }
    .map { line -> line.split(" ").map { it.toInt() } }
    .sumOf { backwardsExtrapolatedValue(it) }

val exampleInput = "0 3 6 9 12 15\n" +
    "1 3 6 10 15 21\n" +
    "10 13 16 21 30 45"

val realInput = File("day9_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
