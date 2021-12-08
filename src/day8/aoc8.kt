package main.day8

import main.util.loadFileAsString
import java.io.FileNotFoundException

fun countUnique(line : String) : Int {
    var (_, right) = line.split(" | ")
    // we ignore left for part 1
    // on right side count words that have 1, 3, 4 or 7 unique characters
    val uniqueCounts = listOf<Long>(2, 3, 4, 7)
    val words = right.split(" ").map { it.chars().distinct().count() }
    return words.count { uniqueCounts.contains(it) }
}

fun add(w1 : String, w2 : String) : String {
    val w1CharSet = w1.toSet()
    val w2CharSet = w2.toSet()
    return w1CharSet.union(w2CharSet).joinToString("")
}

fun subtract(w1 : String, w2 : String) : String {
    val w2CharSet = w2.map { it }.toSet()
    return w1.filter { c -> !w2CharSet.contains(c) }
}

fun isPerm(w1 : String, w2 : String) : Boolean {
    return w1.toSet() == w2.toSet()
}

fun discernNumbers(line : String) : Map<String, Int> {
    var (left, _) = line.split(" | ")
    val uniqueCounts = listOf(2, 3, 4, 7)
    // on right side count words that have 1, 3, 4 or 7 unique characters
    val words = left.split(" ")
    val one = words.first { word -> word.toSet().size == 2 }
    val seven = words.first { word -> word.toSet().size == 3 }
    val four = words.first { word -> word.toSet().size == 4 }
    val eight = words.first { word -> word.toSet().size == 7 }

    var remainder = words.filter { word -> !uniqueCounts.contains(word.toSet().size) }
    // 4 and 7 make a 9
    val three = remainder.first { word -> subtract(word, one).toSet().size == 3 }
    remainder = remainder.filter { !isPerm(it, three) }.toList()

    val nine = add(three, four)
    remainder = remainder.filter { !isPerm(it, nine) }

    val leftSide = subtract(eight, three)
    val zero = add(leftSide, add(one, subtract(three, four)))
    remainder = remainder.filter { !isPerm(it, zero) }

    // at this point one of three numbers has 6 states
    val six = remainder.filter { it.toSet().size == 6 }.first()
    remainder = remainder.filter { !isPerm(it, six) }

    // 2, 5 remaining
    val five = remainder.filter { subtract(nine, it).toSet().size == 1 }.first()
    remainder = remainder.filter { !isPerm(it, five) }

    // 2 remaining
    val two = remainder.first()
    return mapOf(Pair(zero, 0), Pair(one, 1), Pair(two, 2), Pair(three, 3), Pair(four, 4), Pair(five, 5), Pair(six, 6), Pair(seven, 7), Pair(eight, 8), Pair(nine, 9))
}

fun countUniques(lines : String) : Int {
    return lines.split("\n").sumOf { countUnique(it) }
}

fun countMess(lines : String) : Long {
    var result : Long = 0
    for (line in lines.split("\n")) {
        val decodeMap = discernNumbers(line).mapKeys { key -> key.key.toSortedSet().joinToString("") }
        var encodedNumbers = line.split(" | ").get(1)
        var currentNumber : Long = 0
        for (encodedNumber in encodedNumbers.split(" ")) {
            currentNumber = currentNumber * 10 + decodeMap.getOrDefault(encodedNumber.toSortedSet().joinToString(""), 0)
        }
        result += currentNumber
    }

    return result
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = countUniques(loadFileAsString(fileName))
        val part2Result = countMess(loadFileAsString(fileName))
        println("Day 8 part 1 result: $part1Result")
        println("Day 8 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}