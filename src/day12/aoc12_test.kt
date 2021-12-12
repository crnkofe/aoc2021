package main.day12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day12Tests {
    @Test
    fun testRegexParse() {
        var parserRegex = "([a-zA-Z]*)[-]([a-zA-Z]*)".toRegex()
        val result = parserRegex.matchEntire("start-A")?.groupValues
        assertEquals(3, result?.size)
    }

    @Test
    fun testSample1() {
        val sample = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        assertEquals(10, findAllPaths(sample))
        assertEquals(36, findAllPathsWithRepeat(sample))
    }

    @Test
    fun testSample2() {
        val sample = """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent()
        assertEquals(19, findAllPaths(sample))
    }

    @Test
    fun testSample3() {
        val sample = """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent()
        assertEquals(226, findAllPaths(sample))
    }
}