package main.day10

import main.util.loadFileAsString
import java.io.FileNotFoundException

fun findFirstIncorrectCharacter(line : String) : Char? {
    var stack = mutableListOf<Char>()

    val openingChars = listOf('<', '[', '{', '(')
    val closingChars = listOf('>', ']', '}', ')')

    for (c in line) {
        if (openingChars.contains(c)) {
            stack.add(c)
        } else if (stack.isEmpty()) {
            // incomplete line
            return null
        } else if (stack.last() == openingChars[closingChars.indexOf(c)]) {
            stack.removeLast()
        } else {
            // empty stack or invalid current char number
            return c
        }
    }

    // for part 1 we ignore incomplete lines
    return null
}

// autocomplete - incomplete line and return completion string
fun autocompleteLine(line : String) : String {
    var stack = mutableListOf<Char>()

    val openingChars = listOf('<', '[', '{', '(')
    val closingChars = listOf('>', ']', '}', ')')

    for (c in line) {
        if (openingChars.contains(c)) {
            stack.add(c)
        } else if (stack.isEmpty()) {
            // incomplete line
            return stack.reversed().map { c -> closingChars[openingChars.indexOf(c)] }.joinToString("")
        } else if (stack.last() == openingChars[closingChars.indexOf(c)]) {
            stack.removeLast()
        } else {
            // empty stack or invalid current char number
            return ""
        }
    }

    // for part 1 we ignore incomplete lines
    return stack.reversed().map { c -> closingChars[openingChars.indexOf(c)] }.joinToString("")
}

fun calculateSyntaxScore(input : String) : Long {
    val scoreBoard = mapOf(Pair(')', 3.toLong()), Pair(']', 57.toLong()), Pair('}', 1197.toLong()), Pair('>', 25137.toLong()))

    val intermediateResults = input.split("\n")
        .mapNotNull { findFirstIncorrectCharacter(it) }

    return intermediateResults
        .sumOf { scoreBoard.getOrDefault(it, 0.toLong()) }
}

fun calculateSingleAutocompleteScore(line : String) : Long {
    val scoreboard = mapOf(Pair(')', 1), Pair(']', 2), Pair('}', 3), Pair('>', 4))
    var totalScore : Long = 0
    for (c in line) {
        totalScore = totalScore * 5 + scoreboard.getOrDefault(c, 0)
    }
    return totalScore
}

fun calculateAutocompleteScore(input : String) : Long {
    val intermediateResults = input.split("\n")
        .mapNotNull { autocompleteLine(it) }

    val finalScores = intermediateResults
        .filter { it.isNotEmpty() }
        .map { calculateSingleAutocompleteScore(it) }

    return finalScores.sorted()[finalScores.size / 2]
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = calculateSyntaxScore(loadFileAsString(fileName))
        val part2Result = calculateAutocompleteScore(loadFileAsString(fileName))
        println("Day 10 part 1 result: $part1Result")
        println("Day 10 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}