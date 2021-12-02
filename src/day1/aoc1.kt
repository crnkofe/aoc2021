package main.day1

import main.util.loadFileAsStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.charset.Charset

fun countIncreases(file : InputStreamReader) : Int {
    var counter = 0
    var previous : Int? = null
    file.forEachLine {
        var nextVal = it.toInt()
        counter += if ((previous?: Int.MAX_VALUE) < nextVal) 1 else 0
        previous = nextVal
    }
    return counter
}

fun countSlidingWindowIncreases(file : InputStreamReader, windowSize : Int) : Int {
    if (windowSize < 1) {
        throw Exception("Windows size must be >= 1 ($windowSize).")
    }

    var counter : Int = 0
    var lastElements = listOf<Int>()
    file.forEachLine {
        lastElements = lastElements.plus(it.toInt()).takeLast(windowSize+1)
        if (lastElements.take(3).sum() < lastElements.drop(1).take(3).sum()) {
            counter += 1
        }
    }
    return counter
}

fun main(args: Array<String>) {
    var fileName : String = args[0]
    try {
        var day1Counter = countIncreases(loadFileAsStream(fileName))
        println("Day 1 part 2 result: $day1Counter")
        var day1CounterPart2 = countSlidingWindowIncreases(loadFileAsStream(fileName), 3)
        println("Day 1 part 2 result: $day1CounterPart2")
    } catch (e : FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}