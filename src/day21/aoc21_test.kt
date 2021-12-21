package main.day21

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day21Tests {
    @Test
    fun shouldPlaySampleGame() {
        assertEquals(739785, play(200, 4, 8, 1000))
    }

    @Test
    fun shouldPlaySampleGamePart2() {
        var cache = mutableMapOf<DiracState, List<Long>>()
        // 4, 8
        val result = playDiracNaive(1, 0, 0, 3, 7, 21, cache)
        assertEquals(2, result.size)
        assertEquals(444356092776315, result[0])
        assertEquals(341960390180808, result[1])
    }
}
