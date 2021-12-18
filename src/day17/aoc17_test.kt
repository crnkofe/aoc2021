package main.day17

import main.util.Point
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day17Tests {
    @Test
    fun testParseSample() {
        val input = "target area: x=20..30, y=-10..-5"
        val result = parseInput(input)
        assertEquals(Point(20, -10), result.bottomLeft)
        assertEquals(Point(30, -5), result.topRight)

        val n = findValidXs(result)
        assertEquals(29, n.size)
        assertEquals(45, findValidXs(input))
        assertEquals(112, findAllStartingVelocities(input))
    }
}