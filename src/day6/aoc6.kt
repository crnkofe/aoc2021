package main.day6

import main.util.loadFileAsString
import java.io.FileNotFoundException

// simulate puffer fish breeding and return total count after N steps
fun simulatePufferPuffs(state : List<Int>, steps : Int) : Long {
    var initialState = state.groupBy { it }.mapValues { valueList -> valueList.value.size.toLong() }
    // map from puffer fish in state X to count of fish in that state
    var nextState = initialState.toMap()
    for (i in 0 until steps) {
        var countZeroFish = nextState.getOrDefault(0, 0)

        var newNextState = nextState
            .filterKeys { key -> key > 0 }
            .mapKeys { kv -> kv.key - 1 }
            .toMutableMap()

        if (countZeroFish > 0) {
            newNextState[8] = newNextState.getOrDefault(8, 0) + countZeroFish
            newNextState[6] = newNextState.getOrDefault(6, 0) + countZeroFish
        }

        nextState = newNextState
    }
    return nextState.values.sum()
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        var input = loadFileAsString(fileName).split(",").map { s -> s.toInt() }
        var part1Result = simulatePufferPuffs(input, 80)
        var part2Result = simulatePufferPuffs(input, 256)
        println("Day 5 part 1 result: $part1Result")
        println("Day 5 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}
