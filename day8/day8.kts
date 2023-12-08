import java.io.File

data class Node(val key: String, val left: String, val right: String)

fun String.toNode() = this.split(" = ", ", ").let { (key, left, right) ->
    Node(key, left.drop(1), right.dropLast(1))
}

fun String.toNodes() = this.split("\n").filter { it.isNotBlank() }.map { it.toNode() }.associateBy { it.key }

fun part1(input: String): Int {
    val (instructions, nodes) = input.split("\n\n")
        .filter { it.isNotBlank() }
        .let { (instructions, nodes) -> Pair(instructions.toList(), nodes.toNodes()) }

    var steps = 0
    var currentNode = nodes["AAA"]!!
    var currentInstruction = 0
    while(nodes[currentNode.key]!!.key != "ZZZ") {
        when (instructions[currentInstruction]) {
            'L' -> currentNode = nodes[currentNode.left]!!
            'R' -> currentNode = nodes[currentNode.right]!!
        }
        steps++
        currentInstruction = (currentInstruction + 1) % instructions.size
    }
    return steps
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) {
        return a
    }
    return gcd(b, a % b)
}

fun lcm(numbers: List<Long>): Long {
    var result = numbers[0]
    numbers.forEach { result = it * result / gcd(it, result) }
    return result
}

fun part2(input: String): Long {
    val (instructions, nodes) = input.split("\n\n")
        .filter { it.isNotBlank() }
        .let { (instructions, nodes) -> Pair(instructions.toList(), nodes.toNodes()) }

    val steps = nodes.filterKeys { it.last() == 'A' }.map {
        var steps = 0
        var currentInstruction = 0
        var currentNode = it.key
        while(nodes[currentNode]!!.key.last() != 'Z') {
            when (instructions[currentInstruction]) {
                'L' -> currentNode = nodes[currentNode]!!.left
                'R' -> currentNode = nodes[currentNode]!!.right
            }
            steps++
            currentInstruction = (currentInstruction + 1) % instructions.size
        }
        steps.toLong()
    }

    return lcm(steps)
}

val exampleInput = "RL\n" +
    "\n" +
    "AAA = (BBB, CCC)\n" +
    "BBB = (DDD, EEE)\n" +
    "CCC = (ZZZ, GGG)\n" +
    "DDD = (DDD, DDD)\n" +
    "EEE = (EEE, EEE)\n" +
    "GGG = (GGG, GGG)\n" +
    "ZZZ = (ZZZ, ZZZ)"

val exampleInput2 = "LR\n" +
    "\n" +
    "11A = (11B, XXX)\n" +
    "11B = (XXX, 11Z)\n" +
    "11Z = (11B, XXX)\n" +
    "22A = (22B, XXX)\n" +
    "22B = (22C, 22C)\n" +
    "22C = (22Z, 22Z)\n" +
    "22Z = (22B, 22B)\n" +
    "XXX = (XXX, XXX)"

val realInput = File("day8_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput2))

println("Real input part 2: " + part2(realInput))
