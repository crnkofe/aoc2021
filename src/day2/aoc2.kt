package main.day2

import main.util.loadFileAsStream
import main.util.sumList
import java.io.FileNotFoundException
import java.io.InputStreamReader
import kotlin.Exception

// convertToVector converts strings of form 'forward/up/down N' to (x, depth) vector
// down/up go into +/- direction
fun convertToVector(line : String) : List<Int> {
    var (left, right) = line.split(" ")
    var steps = right.toInt()
    return when (left) {
        "forward" -> listOf(steps, 0)
        "up" -> listOf(0, -steps)
        "down" -> listOf(0, steps)
        else -> throw Exception("could not parse line ($line)")
    }
}

fun findEndSubmarinePosition(file : InputStreamReader) : Int  =
    file.buffered(1024).lineSequence()
        .map { convertToVector(it) }
        .fold( listOf(0, 0)) { acc, ints -> sumList(acc, ints) }
        .reduce { acc, i -> acc * i }

// first element of this reducer is now [x, z, aim]
// up/down changes aim
// forward moves by [X + N, -X * aim]
fun sumListWithAim(l1 : List<Int>, line : String) : List<Int> {
    var aim = l1[2]
    var (left, right) = line.split(" ")
    var steps = right.toInt()
    return when (left) {
        "forward" -> listOf(l1[0] + steps, l1[1] + aim * steps, aim)
        "up" -> listOf(l1[0], l1[1], aim - steps)
        "down" -> listOf(l1[0], l1[1], aim + steps)
        else -> throw Exception("Unparseable command $line")
    }
}

fun findEndSubmarinePositionWithAim(file : InputStreamReader) : Int =
    file.buffered(1024).lineSequence()
        .fold( listOf<Int>(0, 0, 0)) { acc, ints -> sumListWithAim(acc, ints) }
        .dropLast(1)
        .reduce { acc, i -> acc * i }

fun main(args: Array<String>) {
    var fileName : String = args[0]
    try {
        var part1Marker = findEndSubmarinePosition(loadFileAsStream(fileName))
        println("Day 2 part 1 result: $part1Marker")
        var part2Marker = findEndSubmarinePositionWithAim(loadFileAsStream(fileName))
        println("Day 2 part 2 result: $part2Marker")
    } catch (e : FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}