package main.day11

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day11Tests {
    @Test
    fun testSimpleFlash() {
        val sample = """
            11111
            19991
            19191
            19991
            11111
        """.trimIndent()
        assertEquals(9, simulateEnlightened(sample, 2).blinked)
    }

    @Test
    fun testSample() {
        val sample = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent()

        assertEquals(1656, simulateEnlightened(sample, 100).blinked)
        assertEquals(195, simulateEnlightened(sample, 1000).allBlinkedSteps.first())
    }
}