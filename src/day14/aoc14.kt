package main.day14

import main.util.loadFileAsString
import java.io.FileNotFoundException

data class Problem14(val polymerFormula : String, val pairs : Map<String, String>)

fun parseInput(s : String) : Problem14 {
    val polymerFormula = s.split("\n").first()
    var pairRegex = "([A-Za-z]+) -> ([A-Za-z]+)".toRegex()
    val pairs = s.split("\n")
        .drop(2)
        .mapNotNull { pairRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .associate { Pair(it[1], it[2]) }
    return Problem14(polymerFormula, pairs)
}

fun evaluatePolymer(problem14 : Problem14, steps : Int) : Long {
    var polymerFormula : String = problem14.polymerFormula
    for (i in 0 until steps) {
        var newPolymerFormula : String = ""
        var lastChars = ""
        for (c in polymerFormula) {
            lastChars += c
            if (lastChars.length > 2) {
                lastChars = lastChars.substring(1)
            }
            val replacement = problem14.pairs.getOrDefault(lastChars, "")
            newPolymerFormula += replacement + c
        }
        polymerFormula = newPolymerFormula
    }
    val test = polymerFormula.map { it }
        .groupBy{ it }
        .mapValues { it.value.size.toLong() }
    return test.values.maxOf { it } - test.values.minOf { it }
}

fun evaluatePolymerSmart(problem14 : Problem14, steps : Int) : Long {
    var polymerFormula : String = problem14.polymerFormula
    // for (i in 0 until steps) {
    var frequencyMap = mutableMapOf<String, Long>()
    var partialFormula = ""
    for (c in polymerFormula.drop(1).dropLast(1)) {
        partialFormula += c
        if (partialFormula.length >= 2) {
            frequencyMap[partialFormula.takeLast(2)] = 1
        }
    }

    var first = polymerFormula.take(2)
    var last = polymerFormula.takeLast(2)

    for (i in 0 until steps) {
        var newFrequencyMap = mutableMapOf<String, Long>()
        for (key in frequencyMap.keys) {
            val replacement = problem14.pairs.getOrDefault(key, "")
            if (replacement == "") {
                continue
            }
            val leftReplacement = key[0] + replacement
            val rightReplacement = replacement + key[1]
            newFrequencyMap[leftReplacement] = newFrequencyMap.getOrDefault(leftReplacement, 0) + frequencyMap.getOrDefault(key, 0)
            newFrequencyMap[rightReplacement] = newFrequencyMap.getOrDefault(rightReplacement, 0) + frequencyMap.getOrDefault(key, 0)
        }

        val lastReplacement = problem14.pairs.getOrDefault(last, "")
        val lastLeftReplacement = last[0] + lastReplacement
        newFrequencyMap[lastLeftReplacement] = 1 + newFrequencyMap.getOrDefault(lastLeftReplacement, 0)

        val firstReplacement = problem14.pairs.getOrDefault(first, "")
        val lastFirstReplacement = firstReplacement + first[1]
        newFrequencyMap[lastFirstReplacement] = 1 + newFrequencyMap.getOrDefault(lastFirstReplacement, 0)

        first = first[0] + firstReplacement
        last = lastReplacement + last[1]
        frequencyMap = newFrequencyMap
    }

    val wat = frequencyMap
        .map { listOf(Pair(it.key[0], it.value)) }
        .flatten()
        .groupBy { it.first }
        .mapValues { p -> p.value.sumOf { it.second } }
        .toMutableMap()
    wat[first[0]] = wat.getOrDefault(first[0], 0) + 1
    wat[last[0]] = wat.getOrDefault(last[0], 0) + 1
    wat[last[1]] = wat.getOrDefault(last[1], 0) + 1
    return wat.values.maxOf { it } - wat.values.minOf { it }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val problem = parseInput(loadFileAsString(fileName))
        val part1Result = evaluatePolymer(problem, 10)
        val part2Result = evaluatePolymerSmart(problem, 40)
        println("Day 14 part 1 result: $part1Result")
        println("Day 14 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}