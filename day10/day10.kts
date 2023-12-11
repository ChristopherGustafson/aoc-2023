import Day10.Direction.EAST
import Day10.Direction.NORTH
import Day10.Direction.SOUTH
import Day10.Direction.WEST
import java.io.File

data class Tile(val x: Int, val y: Int)

enum class Direction { NORTH, SOUTH, WEST, EAST }

fun Tile.move(direction: Direction) =
    when (direction) {
        NORTH -> Tile(this.x, this.y - 1)
        SOUTH -> Tile(this.x, this.y + 1)
        WEST -> Tile(this.x - 1, this.y)
        EAST -> Tile(this.x + 1, this.y)
    }

fun Char.isFacing(direction: Direction) =
    when(direction) {
        NORTH -> this in listOf('L', 'J', '|')
        SOUTH -> this in listOf('F', '7', '|')
        WEST -> this in listOf('7', 'J', '-')
        EAST -> this in listOf('L', 'F', '-')
    }

fun lengthOfPipe(tiles: List<List<Char>>): Pair<Int, Map<Pair<Int, Int>, Boolean>> {
    var start = Tile(0, 0)
    tiles.indices.forEach { y ->
        tiles.first().indices.forEach { x ->
            if (tiles[y][x] == 'S') {
                start = Tile(x, y)
            }
        }
    }
    // TODO: Figure out starting direction programmatically
    var tile = Tile(start.x, start.y + 1)
    var direction = SOUTH
    var pipeLength = 1
    val isLoopPipe =
        tiles.indices.flatMap { y -> tiles.first().indices.map { x -> Pair(x, y) } }.associateWith { false }
            .toMutableMap()
    isLoopPipe[Pair(tile.y, tile.x)] = true

    while (tiles[tile.y][tile.x] != 'S') {
        when (tiles[tile.y][tile.x]) {
            '|' -> tile = if (direction == SOUTH) tile.move(SOUTH).also { direction = SOUTH } else tile.move(NORTH)
                .also { direction = NORTH }

            '-' -> tile = if (direction == WEST) tile.move(WEST).also { direction = WEST } else tile.move(EAST)
                .also { direction = EAST }

            'L' -> tile = if (direction == SOUTH) tile.move(EAST).also { direction = EAST } else tile.move(NORTH)
                .also { direction = NORTH }

            'J' -> tile = if (direction == SOUTH) tile.move(WEST).also { direction = WEST } else tile.move(NORTH)
                .also { direction = NORTH }

            '7' -> tile = if (direction == NORTH) tile.move(WEST).also { direction = WEST } else tile.move(SOUTH)
                .also { direction = SOUTH }

            'F' -> tile = if (direction == NORTH) tile.move(EAST).also { direction = EAST } else tile.move(SOUTH)
                .also { direction = SOUTH }
        }
        pipeLength++
        isLoopPipe[Pair(tile.y, tile.x)] = true
    }
    return Pair(pipeLength, isLoopPipe)
}

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { it.toList() }
        .let { lengthOfPipe(it).first / 2 }

fun findEnclosed(tiles: List<List<Char>>, isLoopPipe: Map<Pair<Int, Int>, Boolean>): Int {

    var enclosed = 0
    tiles.forEachIndexed { y, row ->
        var insideLoop = false
        var prevDirection = EAST
        row.forEachIndexed { x, tile ->
            if (isLoopPipe[Pair(y, x)] == true) {
                when (tile) {
                    // TODO: Figure out start pipe shape programmatically
                    '|', 'S' -> insideLoop = !insideLoop
                    'L' -> prevDirection = SOUTH
                    'F' -> prevDirection = NORTH
                    '7' -> if (prevDirection == SOUTH) insideLoop = !insideLoop
                    'J' -> if (prevDirection == NORTH) insideLoop = !insideLoop
                }
            } else if (insideLoop) {
                enclosed++
            }
        }
    }
    return enclosed
}

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { it.toList() }
        .let { findEnclosed(it, lengthOfPipe(it).second) }

val exampleInput = "..F7.\n" +
    ".FJ|.\n" +
    "SJ.L7\n" +
    "|F--J\n" +
    "LJ..."

val exampleInput2 = "...........\n" +
    ".S-------7.\n" +
    ".|F-----7|.\n" +
    ".||.....||.\n" +
    ".||.....||.\n" +
    ".|L-7.F-J|.\n" +
    ".|..|.|..|.\n" +
    ".L--J.L--J.\n" +
    "..........."

val realInput = File("day10_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Real input part 2: " + part2(realInput))
