package main.day2

import main.day1.countIncreases
import main.util.convertStringToStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day2Tests {
    @Test
    fun testPart1Empty() {
        var testData = ""
        assertEquals(0, findEndSubmarinePosition(convertStringToStream(testData)))
    }

    @Test
    fun testPart1Negative() {
        var testData = """
            down 100
            up -100
            """.trimIndent()
        assertEquals(0, findEndSubmarinePosition(convertStringToStream(testData)))
    }

    @Test
    fun testPart1Sample() {
        var testData = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent()

        assertEquals(150, findEndSubmarinePosition(convertStringToStream(testData)))
    }

    @Test
    fun testPart2Basic() {
        var testData = """
            forward 5
        """.trimIndent()

        assertEquals(0, findEndSubmarinePosition(convertStringToStream(testData)))
    }

    @Test
    fun testPart2AimUp() {
        var testData = """
            forward 5
            up 1
            forward 5
        """.trimIndent()

        // 5 + 5, 0 -5
        assertEquals(-50, findEndSubmarinePositionWithAim(convertStringToStream(testData)))
    }

    @Test
    fun testPart2AimDown() {
        var testData = """
            forward 5
            down 1
            forward 5
        """.trimIndent()

        // 5 + 5, 0 5
        assertEquals(50, findEndSubmarinePositionWithAim(convertStringToStream(testData)))
    }

    @Test
    fun testPart2Sample() {
        var testData = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent()

        assertEquals(900, findEndSubmarinePositionWithAim(convertStringToStream(testData)))
    }
}