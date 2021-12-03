package main.day3

import main.util.loadFileAsStream
import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.Integer.max

data class BitCount(val zeroCount: List<Int>, val lineCount : Int)
data class GammaRate(val rate: Int, val lineLength : Int)

fun toBits(s : String) : List<Int> =
    s.chars()
        .map { x -> if (x == '0'.code) 0 else 1  }
        .toArray()
        .toList()

fun sumBits(acc : BitCount, n : BitCount) : BitCount {
    val accumulatedZeroCount = (0 until max(acc.zeroCount.size, n.zeroCount.size))
        .map { i -> acc.zeroCount.getOrElse(i) {0} + if (n.zeroCount[i] == 0) 1 else 0  }
    return BitCount(accumulatedZeroCount, acc.lineCount + n.lineCount)
}

fun preprocessDiagnosticReport(file : InputStreamReader) : BitCount =
    file.buffered(1024).lineSequence()
        .map { BitCount(toBits(it), 1) }
        .fold( BitCount(listOf(), 0)) { acc, ints -> sumBits(acc, ints) }

fun calculateGammaRate(file : InputStreamReader) : GammaRate {
    var countedZeroes = preprocessDiagnosticReport(file)
    return GammaRate(countedZeroes.zeroCount
        .map {  x -> if (x > countedZeroes.lineCount / 2) 0 else 1}
        .reduce{ x, y -> x.shl(1) + y },
        countedZeroes.zeroCount.size)
}

// invert each bit of integer i up until length l
// Kotlin inv() produces a signed complement and its not what we need
fun invertInt(i : Int, l : Int) : Int {
    var result = 0
    var d = i
    for (a in 0 until l) {
        var m = d % 2
        d = d.shr(1)
        result += (1-m).shl(a)
    }
    return result
}

// for each line of form 01101
// create an array where x[i] = 1
fun calculatePowerConsumption(file : InputStreamReader) : Int  {
    var gammaRate = calculateGammaRate(file)
    return gammaRate.rate * invertInt(gammaRate.rate, gammaRate.lineLength)
}

// applicable to both oxygen generator rating and co2 scrubber rating
fun calculateTheta(data : String, invert : Boolean) : Int {
    var report = data.split("\n").toList()
    var i = 0
    while (report.size > 1) {
        val bitCount = report.fold( BitCount(listOf(), 0)) { acc, ints -> sumBits(acc, BitCount(toBits( ints), 1)) }
        val majorityIndex = if (bitCount.zeroCount[i] <= report.size / 2) 0 else 1
        val majority = if (invert) { 1 - majorityIndex } else majorityIndex
        report = report.filter { el -> el[i] == if (majority == 0) '1' else '0'}
        i++
    }
    return report[0].toInt(2)
}

fun calculateLifeSupportRating(data : String) : Int {
    var oxygenGeneratorRating = calculateTheta(data, false)
    var co2ScrubberRating = calculateTheta(data, true)
    return oxygenGeneratorRating * co2ScrubberRating
}

fun main(args: Array<String>) {
    var fileName : String = args[0]
    try {
        var part1Marker = calculatePowerConsumption(loadFileAsStream(fileName))
        println("Day 3 part 1 result: $part1Marker")
        var part2Marker = calculateLifeSupportRating(loadFileAsString(fileName))
        println("Day 3 part 2 result: $part2Marker")
    } catch (e : FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}