package main.day5

import main.util.Point
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day5Tests {
    @Test
    fun generateLineTest() {
        assertEquals(listOf(Point(1, 1)), generateLine( Point(1, 1), Point(1, 1)))
        assertEquals(listOf(Point(-1, 1), Point(0, 1), Point(1, 1)), generateLine( Point(1, 1), Point(-1, 1)))
        assertEquals(listOf(Point(1, -1), Point(1, 0), Point(1, 1)), generateLine( Point(1, 1), Point(1, -1)))
    }

    @Test
    fun testIntersections() {
        var line1 = listOf(Point(1, 1), Point(1, 1))
        assertEquals(1, findIntersections(listOf(line1, line1), ::generateLine))

        var line2 = listOf(Point(1, 1), Point(5, 1))
        assertEquals(5, findIntersections(listOf(line2, line2), ::generateLine))
    }

    @Test
    fun testSample() {
        val sample = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent()

        var straightLines = parseLines(sample).filter { line -> line[0].x == line[1].x || line[0].y == line[1].y  }
        assertEquals(5, findIntersections(straightLines, ::generateLine))
    }

    @Test
    fun generateDiagonalLineTest() {
        assertEquals(listOf(Point(1, 1)), generateDiagonalLine( Point(1, 1), Point(1, 1)))
        assertEquals(listOf( Point(1, 1), Point(0, 1), Point(-1, 1)), generateDiagonalLine( Point(1, 1), Point(-1, 1)))
        assertEquals(listOf(Point(1, 1), Point(1, 0), Point(1, -1)), generateDiagonalLine( Point(1, 1), Point(1, -1)))
    }

    @Test
    fun testSamplePart2() {
        val sample = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent()

        var allLines = parseLines(sample)
        assertEquals(12, findIntersections(allLines, ::generateDiagonalLine))
    }
}