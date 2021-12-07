package main.day7

import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.lang.Integer.max
import kotlin.math.abs

fun calculateUsedFuel(position : Int, originalPositions: List<Int>, cost : Map<Int, Long>) : Long {
    return (originalPositions.indices).sumOf {
        cost.getOrDefault(abs(originalPositions[it] - position), abs(originalPositions[it] - position).toLong()) }
}

fun bisectCrabs(originalPositions : List<Int>, cost : Map<Int, Long>) : Long {
    var r = originalPositions.maxOrNull() ?: 0
    var l = originalPositions.minOrNull() ?: 0

    while (l != r) {
        var rSum = calculateUsedFuel(r, originalPositions, cost)
        var lSum = calculateUsedFuel(l, originalPositions, cost)

        val mid = (l + r).div(2)
        var midSum = calculateUsedFuel(mid, originalPositions, cost)

        val distLeft = lSum - midSum
        val distRight = rSum - midSum
        if ((abs(r - l) == 1) && (rSum < lSum)) {
            l = r
        } else if (distLeft < distRight) {
            r = mid
        } else {
            l = mid
        }
    }
    return calculateUsedFuel(l, originalPositions, cost)
}

fun computeCost(crabPositions : List<Int>) : Map<Int, Long> {
    var precomputedCost = mutableMapOf<Int, Long>()
    var r = crabPositions.maxOrNull() ?: 0
    var l = crabPositions.minOrNull() ?: 0
    var acc : Long = 0
    for (i in 1..max(r, l)) {
        acc += i
        precomputedCost[i] = acc
    }
    return precomputedCost
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        var crabPositions = loadFileAsString(fileName).split(",").map { s -> s.toInt() }

        var part1Result = bisectCrabs(crabPositions, mapOf())
        var part2Result = bisectCrabs(crabPositions, computeCost(crabPositions))
        println("Day 7 part 1 result: $part1Result")
        println("Day 7 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}