package main.day14

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day14Tests {
    @Test
    fun testRegexParse() {
        val input = """
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent()
        val problem = parseInput(input)
        assertEquals("NNCB", problem.polymerFormula)
        assertEquals(16, problem.pairs.size)

        assertEquals(1588, evaluatePolymer(problem, 10))
        assertEquals(1588, evaluatePolymerSmart(problem, 10))
        assertEquals(2188189693529, evaluatePolymerSmart(problem, 40))
    }
}