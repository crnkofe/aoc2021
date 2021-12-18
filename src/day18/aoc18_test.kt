package main.day18

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day18Tests {
    @Test
    fun shouldParseSamples() {
        val input = """
            [1,2]
            [[1,2],3]
            [9,[8,7]]
            [[1,9],[8,5]]
            [[[[1,2],[3,4]],[[5,6],[7,8]]],9]
            [[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
            [[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
        """.trimIndent()

        for (line in input.split("\n").filter { it != "" }) {
            assertEquals(line, parseInput(line).toString())
        }
    }

    @Test
    fun shouldHandleDepth() {
        val input = "[[[[[9,8],1],2],3],4]"
        val parsedInput = parseInput(input)
        val nums = parsedInput.extractNumbers()
        assertEquals("[[[[0,9],2],3],4]", parsedInput.reduce(1, nums, true).toString())

        var input1 = "[7,[6,[5,[4,[3,2]]]]]"
        val parsedInput1 = parseInput(input1)
        val nums1 = parsedInput1.extractNumbers()
        assertEquals("[7,[6,[5,[7,0]]]]", parsedInput1.reduce(1, nums1, true).toString())

        var input2 = "[[6,[5,[4,[3,2]]]],1]"
        assertEquals("[[6,[5,[7,0]]],3]", evaluate(input2))

        var input3 = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
        val parsedInput3 = parseInput(input3)
        val nums3 = parsedInput3.extractNumbers()
        assertEquals(listOf("3", "2", "1", "7", "3", "6", "5", "4", "3", "2"), nums3.map { it.toString() })
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", parsedInput3.reduce(1, nums3, true).toString())
    }

    @Test
    fun shouldHandleComplexExample() {
        var input = "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", evaluate(input))
    }

    @Test
    fun shouldPassSimpleList() {
        var input = """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
        """.trimIndent()
        assertEquals(445, evaluateFile(input))
    }

    @Test
    fun shouldPassAMediumList() {
        var input = """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            [5,5]
        """.trimIndent()
        assertEquals(791, evaluateFile(input))
    }

    @Test
    fun shouldPassAComplexList() {
        var input = """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            [5,5]
            [6,6]
        """.trimIndent()
        assertEquals(1137, evaluateFile(input))
    }

    @Test
    fun testFooBar() {
        var input = "[[[[4,0],[5,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]"
        evaluate(input)
    }

    @Test
    fun shouldPassSlightlyLargerExample() {
        // tODO: FIX me
//        var input = """
//            [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
//            [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
//        """.trimIndent()

        /**
         * [[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,4]],[[0,[7,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,4]],[[7,0],[15,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,4]],[[7,[7,8]],0]],[22,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,4]],[[14,0],8]],[22,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,4]],[[[7,7],0],0],8]],[22,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[[[4,0],[5,11]],[[0,7],0],8]],[22,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         * [[ [[4,0],[5,11]] , [[0,7],0] ,8]],[22,[[[3,7],[4,3]],[[6,3],[8,8]]]]]
         */

//        var input = """
//            [[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
//            [2,9]
//        """.trimIndent()
//
        var input = """
            [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
            [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
            [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
            [7,[5,[[3,8],[1,4]]]]
            [[2,[2,2]],[8,[8,1]]]
            [2,9]
            [1,[[[9,3],9],[[9,0],[0,7]]]]
            [[[5,[7,4]],7],1]
            [[[[4,2],2],6],[8,7]]
            """.trimIndent()
        assertEquals(3377, evaluateFile(input))
    }

    @Test
    fun shouldPassHomework() {
        var input = """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent()
        assertEquals(4140, evaluateFile(input))
    }
}