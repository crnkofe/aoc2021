package main.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.lang.Integer.max
import java.nio.charset.Charset

data class Point(var x: Int, var y: Int) {
    fun sgn() : Point {
        return Point(Integer.signum(this.x), Integer.signum(this.y))
    }
    operator fun plus(inc: Point) = Point(this.x + inc.x, this.y + inc.y)
    operator fun minus(inc: Point): Point = Point(this.x - inc.x, this.y - inc.y)
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