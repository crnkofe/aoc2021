package main.day9

import main.util.Point
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day9Tests {
    @Test
    fun testToBoard() {
        val sample1 = """
            2199943210
            """.trimIndent()
        val board = toBoard(sample1)
        assertEquals(1, board.points.size)
        assertEquals(listOf(2, 1, 9, 9, 9, 4, 3, 2, 1, 0), board.points[0])
    }

    @Test
    fun testLowPoint() {
        val sample1 = """
            222
            212
            222
            """.trimIndent()
        val board = toBoard(sample1)
        assertTrue(isLowPoint(Point(1, 1), board))
        assertFalse(isLowPoint(Point(0, 0), board))
        assertFalse(isLowPoint(Point(2, 2), board))
    }

    @Test
    fun testSample() {
        val sample1 = """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
        """.trimIndent()
        assertEquals(15, calculateRiskLevel(sample1))
        assertEquals(1134, findBasinsFactor(sample1))
    }
}