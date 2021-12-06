package main.day6

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day6Tests {
    @Test
    fun testSample() {
        var input = listOf(3,4,3,1,2)
        assertEquals(26, simulatePufferPuffs(input, 18))
        assertEquals(5934, simulatePufferPuffs(input, 80))
    }
}
