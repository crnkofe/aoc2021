package main.day25

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day25Tests {
    @Test
    fun testSample() {
        val input = """
            v...>>.vv>
            .vv>>.vv..
            >>.>v>...v
            >>v>>.>.v.
            v>v.vv.v..
            >.>>..v...
            .vv..>.>v.
            v.v..>>v.v
            ....v..v.>
        """.trimIndent()
        val map = parseCucumberMap(input)
        assertEquals(49, map.cucumbers.size)
        assertEquals(10, map.width)
        assertEquals(9, map.height)

        assertEquals(58, simulate(map))
    }

}