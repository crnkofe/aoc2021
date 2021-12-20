package main.day20

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException
import kotlin.test.assertEquals

fun parseAlgo(s: String) : String {
    return s.split("\n").takeWhile { it != "" }.joinToString("")
}

fun parseImage(s : String) : Map<Point, Char> {
    // TODO: fix image orientation
    var imageMap = mutableMapOf<Point, Char>()
    for (row in s.split("\n").dropWhile { it != "" }.filter { it != "" }.withIndex()) {
        for (col in row.value.withIndex()) {
            imageMap[Point(col.index, -row.index)] = col.value
        }
    }
    return imageMap
}

fun toInt(s : String) : Int {
    var num = 0
    for (c in s) {
        num *= 2
        num += (if (c == '1') 1 else 0)
    }
    return num
}

fun enhance(image: Map<Point, Char>, algo : String, step : Int) : Map<Point, Char> {
    val bottomLeft = Point(image.keys.map { it.x }.minOf { it }, image.keys.map { it.y }.minOf { it })
    val topRight = Point(image.keys.map { it.x }.maxOf { it }, image.keys.map { it.y }.maxOf { it })

    val window = listOf<Point>(
        Point(-1, 1), Point(0, 1), Point(1, 1),
        Point(-1, 0), Point(0, 0), Point(1, 0),
        Point(-1, -1), Point(0, -1), Point(1, -1),
    )

    var newImage = mutableMapOf<Point, Char>()

    val d = 1
    for (row in bottomLeft.y-d..topRight.y+d) {
        for (col in bottomLeft.x-d..topRight.x+d) {
            val index = toInt(window
                .map { image.getOrDefault(Point(col, row) + it, if ((step % 2) == 1) '.' else '#') }
                .map { if (it == '#') '1' else '0' }
                .joinToString(""))

            newImage[Point(col, row)] = algo[index] ?: throw Exception("")
        }
    }
    return newImage
}

fun draw(image: Map<Point, Char>) {
    val topLeft = Point(image.keys.map { it.x }.minOf { it }, image.keys.map { it.y }.maxOf { it })
    val bottomRight = Point(image.keys.map { it.x }.maxOf { it }, image.keys.map { it.y }.minOf { it })

    var strs = mutableListOf<String>()
    for (y in bottomRight.y..topLeft.y) {
        strs.add( (topLeft.x..bottomRight.x).map { image.getOrDefault(Point(it, y), '.') }.joinToString(""))
    }
    strs.reversed().forEach { println(it) }
    println()
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val algo = parseAlgo(loadFileAsString(fileName))
        var img = parseImage(loadFileAsString(fileName))

        println(img.values.filter { it == '#' }.size)
        for (i in 0 until 2) {
            img = enhance(img, algo, i+1)
        }

        var img2 = parseImage(loadFileAsString(fileName))
        for (i in 0 until 50) {
            img2 = enhance(img2, algo, i+1)
        }

        val part1Result = img.values.filter { it == '#' }.size
        println("Day 20 part 1 result: $part1Result")
        val part2Result = img2.values.filter { it == '#' }.size
        println("Day 20 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}