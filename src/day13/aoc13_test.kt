package main.day13

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day13Tests {
    @Test
    fun testRegexParse() {
        val input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()

        val result = parseProblem(input)
        assertEquals(18,  result.visiblePoints.size)
        assertEquals(2,  result.instructions.size)
        assertEquals(Fold(0, 7), result.instructions[0])
        assertEquals(Fold(5, 0), result.instructions[1])

        assertEquals(17, fold(result, 1).size)
    }
}