package main.day10

import main.util.loadFileAsString
import java.io.FileNotFoundException

val openingChars = listOf('<', '[', '{', '(')
val closingChars = listOf('>', ']', '}', ')')

data class SyntaxError(val c : Char?, val completion : String)

fun completeStack(chars : List<Char>) : String {
    return chars.reversed().map { c -> closingChars[openingChars.indexOf(c)] }.joinToString("")
}

// autocomplete - incomplete line and return completion string
fun autocompleteLine(line : String) : SyntaxError {
    var stack = mutableListOf<Char>()
    for (c in line) {
        if (openingChars.contains(c)) {
            stack.add(c)
        } else if (stack.isEmpty()) {
            // incomplete line
            return SyntaxError(c, completeStack(stack))
        } else if (stack.last() == openingChars[closingChars.indexOf(c)]) {
            stack.removeLast()
        } else {
            // invalid current char
            return SyntaxError(c, "")
        }
    }

    // for part 1 we ignore incomplete lines
    return SyntaxError(null, completeStack(stack))
}

fun calculateSyntaxScore(input : String) : Long {
    val scoreBoard = mapOf<Char, Long>(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    return input.split("\n")
        .mapNotNull { autocompleteLine(it).c }
        .sumOf { scoreBoard.getOrDefault(it, 0.toLong()) }
}

fun calculateSingleAutocompleteScore(line : String) : Long {
    val scoreboard = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    return line
        .map { scoreboard.getOrDefault(it, 0) }
        .fold(0) { acc, it -> acc * 5 + it }
}

fun calculateAutocompleteScore(input : String) : Long {
    val scores = input.split("\n")
        .map { autocompleteLine(it) }
        .filter { it.completion.isNotEmpty() }
        .map { calculateSingleAutocompleteScore(it.completion) }

    return scores.sorted()[scores.size / 2]
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