package main.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.lang.Integer.max
import java.nio.charset.Charset
import kotlin.math.abs

data class Point(var x: Int, var y: Int) {
    fun sgn() : Point {
        return Point(Integer.signum(this.x), Integer.signum(this.y))
    }
    operator fun plus(inc: Point) = Point(this.x + inc.x, this.y + inc.y)
    operator fun minus(inc: Point): Point = Point(this.x - inc.x, this.y - inc.y)

    fun neighbours4() : List<Point> {
        val neighbourPoints = listOf(Point(1, 0), Point(-1, 0), Point(0, 1), Point(0, -1),)
        return neighbourPoints.map { this + it }
    }

    fun neighbours8() : List<Point> {
        var neighbourPoints = mutableListOf<Point>()
        for (i in -1..1) {
            for (j in -1 .. 1) {
                if ((i == 0) && (j == 0)) {
                    continue
                }
                neighbourPoints.add(this + Point(i, j))
            }
        }
        return neighbourPoints
    }

}

data class PointIterator(val b : Board, val p : Point) : Iterator<Point> {
    override fun hasNext(): Boolean {
        return b.at(p) != null
    }

    override fun next(): Point {
        val row = b.points[p.y] ?: throw Exception("unknown next point")
        val pc = Point(p.x, p.y)
        p.x += 1
        if (p.x >= row.size) {
            p.x = 0
            p.y += 1
        }
        return pc
    }
}

data class Board(var points: MutableList<MutableList<Int>>) {
    fun points() : Iterator<Point> {
        return PointIterator(this, Point(0, 0))
    }

    fun at (p: Point) : Int? {
        return points
            .getOrElse(p.y) { listOf() }
            .getOrNull(p.x)
    }

    fun inc (p: Point, by : Int) : Int? {
        var row = this.points.getOrNull(p.y)
        if (row != null) {
            if ((p.x >= 0) && (p.x < row.size)) {
                row[p.x] += by
                return row[p.x]
            }
        }
        return null
    }

    fun set (p: Point, to : Int) : Int? {
        var row = this.points.getOrNull(p.y)
        if (row != null) {
            if ((p.x >= 0) && (p.x < row.size)) {
                row[p.x] = to
                return row[p.x]
            }
        }
        return null
    }

    override fun toString() : String {
        return this.points
            .joinToString("\n") { row -> row.map { it.digitToChar() }.joinToString("") }
    }
}

fun manhattan(p1: Point, p2: Point) : Int {
    return abs(p2.x - p1.x) + abs(p2.y - p1.y)
}

fun sumList(l1 : List<Int>, l2 : List<Int>) : List<Int> =
    (0 until max(l1.size, l2.size)).map { i -> l1.getOrElse(i) { 0 } + l2.getOrElse(i) { 0 } }

fun loadFileAsStream(fileName : String) : InputStreamReader {
    return File(fileName).reader(Charset.forName("ASCII"))
}

fun loadFileAsString(fileName : String) : String {
    return File(fileName).reader(Charset.forName("ASCII")).readText()
}

fun convertStringToStream(data : String) : InputStreamReader {
    var byteArrayInputStream = ByteArrayInputStream(data.toByteArray(Charset.defaultCharset()))
    return InputStreamReader(byteArrayInputStream)
}