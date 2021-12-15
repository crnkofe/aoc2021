package main.day15

import main.util.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day15Tests {
    @Test
    fun testParse() {
        val input = """
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
        """.trimIndent()
        val board = parseRiskBoard(input)
        assertEquals(6, board.at(Point(2, 0)) ?: 0)
        assertEquals(10, board.points.size)

        val goal = Point(board.points[0].size - 1, board.points.size - 1)
        assertEquals(40, findShortestPath(Point(0, 0), goal, board))
    }
}