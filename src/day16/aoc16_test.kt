package main.day16

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day16Tests {
    @Test
    fun testParseSample() {
        var input = "D2FE28"
        val p = parsePacket(input,0)
        assertEquals(6, p.version)
        assertEquals(4, p.packetID)
        assertEquals(2021, p.literal)
    }

    @Test
    fun testParseSample1() {
        var input = "38006F45291200"
        val p = parsePacket(input,0)
        assertEquals(1, p.version)
        assertEquals(6, p.packetID)
        assertEquals(2, p.subpackets.size)
    }

    @Test
    fun testParseSample2() {
        val input = "EE00D40C823060"
        val p = parsePacket(input,0)
        assertEquals(7, p.version)
        assertEquals(3, p.packetID)
        assertEquals(3, p.subpackets.size)
    }

    @Test
    fun testParseSample3() {
        val input = "8A004A801A8002F478"
        val p = parsePacket(input,0)
        assertEquals(4, p.version)
        assertEquals(2, p.packetID)
        assertEquals(1, p.subpackets.size)

        assertEquals(1, p.subpackets[0].version)
        assertEquals(1, p.subpackets[0].subpackets.size)

        assertEquals(5, p.subpackets[0].subpackets[0].version)
        assertEquals(1, p.subpackets[0].subpackets.size)

        assertEquals(6, p.subpackets[0].subpackets[0].subpackets[0].version)
        assertEquals(16, sumVersions(p))
    }

    @Test
    fun testParseSamples() {
        val input0 = "620080001611562C8802118E34"
        assertEquals(12, sumVersions(parsePacket(input0, 0)))

        val input = "C0015000016115A2E0802F182340"
        assertEquals(23, sumVersions(parsePacket(input, 0)))

        val input1 = "A0016C880162017C3686B18A3D4780"
        assertEquals(31, sumVersions(parsePacket(input1, 0)))
    }
}