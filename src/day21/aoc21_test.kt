package main.day21

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day21Tests {
    @Test
    fun shouldPlaySampleGame() {
        assertEquals(739785, play(200, 4, 8))
    }
}
