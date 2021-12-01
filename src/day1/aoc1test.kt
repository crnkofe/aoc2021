package main

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.*
import java.nio.charset.Charset
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day1Tests {
    @Test
    fun testPart1BasicIncrease() {
        var testData = """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(7, countIncreases(InputStreamReader(byteArrayInputStream)))
    }

    @Test
    fun testPart1OneLiner() {
        var testData = """
            199
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(0, countIncreases(InputStreamReader(byteArrayInputStream)))
    }

    @Test
    fun testPart1OrderOfIncrease() {
        var testData = """
            199
            1999
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(1, countIncreases(InputStreamReader(byteArrayInputStream)))
    }

    @Test
    fun testPart1Alpha() {
        var testData = """
            299
            1999
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(1, countIncreases(InputStreamReader(byteArrayInputStream)))
    }


    @Test
    fun testPart2Simple() {
        var testData = """
            1
            2
            3
            4
            5
            6
            7
            8
            9
            10
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(7, countSlidingWindowIncreases(InputStreamReader(byteArrayInputStream), 3))
    }

    @Test
    fun testPart2Sample() {
        var testData = """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
        """.trimIndent()

        var byteArrayInputStream = ByteArrayInputStream(testData.toByteArray(Charset.defaultCharset()))
        assertEquals(5, countSlidingWindowIncreases(InputStreamReader(byteArrayInputStream), 3))
    }

}
