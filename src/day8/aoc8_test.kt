package main.day8

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day8Tests {
    @Test
    fun testSample() {
        val sample = """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
        """.trimIndent()

        assertEquals(26, countUniques(sample))
    }

    @Test
    fun testSampleMess() {
        val sample = """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
        """.trimIndent()

        assertEquals(61229, countMess(sample))
    }

    @Test
    fun testAdd() {
        assertEquals("abc", add("a", "bc"))
        assertEquals("abc", add("abc", "abc"))
    }

    @Test
    fun testIs() {
        assertTrue(isPerm("abc", "bca"))
        assertTrue(isPerm("", ""))
        assertFalse(isPerm("a", "b"))
    }

    @Test
    fun testSubtract() {
        assertEquals("c", subtract("abc", "ab"))
        assertEquals("", subtract("abc", "abcd"))
    }

    @Test
    fun testDiscernNumbers() {
        val input = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
        var results = discernNumbers(input)
        var invertedResults = results.map { entry -> Pair(entry.value, entry.key) }.toMap()
        assertTrue(isPerm("cagedb", invertedResults[0].orEmpty()))
        assertTrue(isPerm("ab", invertedResults[1].orEmpty()))
        assertTrue(isPerm("gcdfa", invertedResults[2].orEmpty()))
        assertTrue(isPerm("fbcad", invertedResults[3].orEmpty()))
        assertTrue(isPerm("eafb", invertedResults[4].orEmpty()))
        assertTrue(isPerm("cdfbe", invertedResults[5].orEmpty()))
        assertTrue(isPerm("cdfgeb", invertedResults[6].orEmpty()))
        assertTrue(isPerm("dab", invertedResults[7].orEmpty()))
        assertTrue(isPerm("acedgfb", invertedResults[8].orEmpty()))
        assertTrue(isPerm("cefabd", invertedResults[9].orEmpty()))
    }
}