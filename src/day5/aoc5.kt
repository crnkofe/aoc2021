package main.day5

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.lang.Integer.*

// v1 line generator generates a list of points (including ends) between Point p1 and p2
// note: only handles horizontal lines
fun generateLine(p1: Point, p2: Point) : List<Point> {
    var line = mutableListOf<Point>()
    for (x in min(p1.x, p2.x)..max(p1.x, p2.x)){
        for (y in min(p1.y, p2.y)..max(p1.y, p2.y)) {
            line.add(Point(x, y))
        }
    }
    return line
}

// v2 line generator generates a list of points (including ends) between Point p1 and p2
// note: handles horizontal and diagonal (45 degree) lines
fun generateDiagonalLine(p1: Point, p2: Point) : List<Point> {
    var line = mutableListOf(p1)
    var delta = (p2 - p1).sgn()
    var p = p1
    while (p != p2) {
        p += delta
        line.add(Point(p.x, p.y))
    }
    return line
}

// find intersections between lines
// only return a counter counting the points where intersection occurs at least once
fun findIntersections(lines : List<List<Point>>, lineGenerator : (Point, Point) -> List<Point>) : Int =
    lines.asSequence().map { line -> lineGenerator(line[0], line[1]) }.flatten()
        .groupBy { it }
        .filter { it.value.size > 1 }
        .count()

// parses entries of form 0,9 -> 1,9 to lists of 2 points List<Point>
// ignores diagonal lines
fun parseLines(s : String) : List<List<Point>> {
    var parserRegex = "([0-9]+?),([0-9]+?) -> ([0-9]+?).([0-9]+?)".toRegex()
    return s.split("\n")
        .mapNotNull { rawLine -> parserRegex.matchEntire(rawLine) }
        .map { match -> match.groupValues.toList() }
        .map { matches -> listOf(Point(matches[1].toInt(), matches[2].toInt()), Point(matches[3].toInt(), matches[4].toInt())) }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        var lines = parseLines(loadFileAsString(fileName))
        var straightLines = lines.filter { line -> line[0].x == line[1].x || line[0].y == line[1].y  }
        var part1Marker = findIntersections(straightLines, ::generateLine)
        var part2Marker = findIntersections(lines, ::generateDiagonalLine)
        println("Day 5 part 1 result: $part1Marker")
        println("Day 5 part 2 result: $part2Marker")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}
