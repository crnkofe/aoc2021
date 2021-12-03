package main.day3

import main.util.convertStringToStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day3Tests {
    @Test
    fun testInvert() {
        assertEquals(0, invertInt(0xFF, 8))
        assertEquals(0xFF, invertInt(0, 8))
    }

    @Test
    fun testPart1ToBits() {
        assertEquals(listOf(1, 1, 1, 1, 1), toBits("11111"))
        assertEquals(listOf(0, 0, 0, 0, 0), toBits("00000"))
    }

    @Test
    fun testPart1SimpleGammaRate() {
        var testData = """
            01000
            01000
            00001
            """.trimIndent()
        assertEquals(BitCount(listOf(3, 1, 3, 3, 2), 3), preprocessDiagnosticReport(convertStringToStream(testData)))
        assertEquals(GammaRate(8, 5), calculateGammaRate(convertStringToStream(testData)))
        assertEquals(184, calculatePowerConsumption(convertStringToStream(testData)))
    }

    @Test
    fun testPart1SampleGammaRate() {
        var testData = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """.trimIndent()
        assertEquals(BitCount(listOf(5, 7, 4, 5, 7), 12), preprocessDiagnosticReport(convertStringToStream(testData)))
        assertEquals(GammaRate(22, 5), calculateGammaRate(convertStringToStream(testData)))
        assertEquals(198, calculatePowerConsumption(convertStringToStream(testData)))
    }

    @Test
    fun testPart2OxygenGenerator() {
        var testData = """
            00100
            """.trimIndent()
        assertEquals(4, calculateTheta(testData, false))
    }

    @Test
    fun testPart2SampleOxygenGenerator() {
        var testData = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
            """.trimIndent()
        assertEquals(23, calculateTheta(testData, false))
        assertEquals(10, calculateTheta(testData, true))
        assertEquals(230, calculateLifeSupportRating(testData))
    }
}