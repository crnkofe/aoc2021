package main.day7

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day7Tests {
    @Test
    fun testSample() {
        val sampleData = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }.toList()
        assertEquals(37, bisectCrabs(sampleData, mapOf()))
    }

    @Test
    fun testSamplePart2() {
        val sampleData = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }.toList()
        assertEquals(168, bisectCrabs(sampleData, computeCost(sampleData)))
    }
}
