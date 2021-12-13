package main.day13

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.lang.Integer.max
import java.lang.Integer.min

data class Problem13(val visiblePoints : Set<Point>, val instructions : List<Fold>)
data class Fold(var alongX : Int, var alongY : Int)

fun parseFold(axis : String, num : Int) : Fold =
    if (axis == "x") {
        Fold(num, 0)
    } else {
        Fold(0, num)
    }

fun parseProblem(s : String) : Problem13 {
    var pointParserRegex = "([0-9]+),([0-9]+)".toRegex()
    val points = s.split("\n")
        .takeWhile { s.isNotEmpty() }
        .mapNotNull { pointParserRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .map { Point(it[1].toInt(), it[2].toInt()) }

    var foldRegex = "fold along ([x|y])=([0-9]+)".toRegex()
    val folds = s.split("\n")
        .dropWhile { it != "" }
        .mapNotNull { foldRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .map { parseFold(it[1], it[2].toInt()) }

    return Problem13(points.toSet(), folds)
}

fun foldPoint(p : Point, f : Fold) : Point =
    if ((f.alongX != 0) && (f.alongX < p.x)) {
        // fold left
        Point(f.alongX - (p.x - f.alongX), p.y)
    } else if ((f.alongY != 0) && (f.alongY < p.y)) {
        // fold top
        Point(p.x, f.alongY - (p.y - f.alongY))
    } else {
        p
    }

fun fold(input : Problem13, steps : Int) : Set<Point> {
    return input.instructions.take(steps)
        .fold(input.visiblePoints) { acc, fold -> acc.map { foldPoint(it, fold) }.toSet() }
}

fun printPaper(points : Set<Point>) {    
    var minP = Point(Int.MAX_VALUE, Int.MAX_VALUE)
    var maxP = Point(Int.MIN_VALUE, Int.MIN_VALUE)
    for (p in points) {
        minP.x = min(p.x, minP.x)
        minP.y = min(p.y, minP.y)

        maxP.x = max(p.x, maxP.x)
        maxP.y = max(p.y, maxP.y)
    }

    for (y in minP.y .. maxP.y) {
        println((minP.x..maxP.x).map {
            if (points.contains(Point(it, y))) {
                "# "
            } else {
                ". "
            }
        }.joinToString(""))
    }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val problem = parseProblem(loadFileAsString(fileName))
        val part1Result = fold(problem, 1).size
        val part2Result = fold(problem, problem.instructions.size+1)
        println("Day 13 part 1 result: $part1Result")
        println("Day 13 part 2 result: ${part2Result.size}")
        printPaper(part2Result)
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}