package main.day21

import main.util.loadFileAsString
import java.io.FileNotFoundException

fun roll(previous : Int) : Int{
    return previous+1
}

data class DiracState(val p1pos : Int, val p2pos : Int, val p1score : Int, val p2score : Int, val playerIndex : Int)

fun generateRollScores(pos : Int) : List<Int> {
    var poss = mutableListOf<Int>()
    for (i in 1..3) {
        for (j in 1..3) {
            for (k in 1..3) {
                poss.add((pos + i + j + k) % 10)
            }
        }
    }
    /*
    for (i in 1..3) {
        for (j in 1..3) {
            poss.add((pos + i + j) % 10)
        }
    }
     */

    return poss
}

fun playDirac(rounds : Int, start1 : Int, start2 : Int, score : Int) : Long {

    // DP technique
    // idea count number of times player 1 wins at score 20 and player 2 wins if score == 20
    // then go backwards -- count number of times player 2 wins at 19 (directly, or indirectly)
    // a die roll wins directly if score is larger than 21 (after player 1 rolls or player 2 rolls)
    // a die roll wins indirectly if if score is still less than 21

    var player1WinsCount = mutableMapOf<Int, Long>()
    var player2WinsCount = mutableMapOf<Int, Long>()

    // this is played on a 0-9 board to make modulos easier
    var player1Pos = start1 - 1
    var player2Pos = start2 - 1

    for (round in 1..2) {
        for (roll1 in generateRollScores(player1Pos)) {
            for (roll2 in generateRollScores(player2Pos)) {
                val score1 = roll1 + 1
                if (score1 > round) {
                    player1WinsCount[round] = player1WinsCount.getOrDefault(round, 0) + 1
                    continue
                }
                val score2 = roll2 + 1
                if (score2 > round) {
                    player2WinsCount[round] = player2WinsCount.getOrDefault(round, 0) + 12
                    continue
                }
            }
        }
    }

    println("$player1WinsCount, $player2WinsCount")
    return 0
}


fun playDiracNaive(round : Int, score1 : Int, score2: Int, start1 : Int, start2 : Int, limit : Int, cache : MutableMap<DiracState, List<Long>>) : List<Long> {
    // cache needs to be separated by player
    val ds = DiracState(start1, start2, score1, score2, round % 2)
    if (cache.containsKey(ds)) {
        return cache[ds] ?: listOf(0, 0)
    }

    var player1Turn = (round % 2) == 1
    // not ideal but we check if current player is winner
    if (!player1Turn && (score1 >= limit)) {
        return listOf(1, 0)
    }
    if (player1Turn && (score2 >= limit)) {
        return listOf(0, 1)
    }

    var player1WinsCount: Long = 0
    var player2WinsCount: Long = 0

    for (roll in generateRollScores(if (player1Turn) start1 else start2)) {
        val newScore = (roll + 1)
        val newScore1 = score1 + if (player1Turn) newScore else 0
        val newScore2 = score2 + if (player1Turn) 0 else newScore
        val newStart1 = if (player1Turn) roll else start1
        val newStart2 = if (player1Turn) start2 else roll

        val scores = playDiracNaive(round+1, newScore1, newScore2, newStart1, newStart2, limit, cache)
        player1WinsCount += scores[0]
        player2WinsCount += scores[1]
    }

    cache[ds] = listOf(player1WinsCount, player2WinsCount)
    return listOf(player1WinsCount, player2WinsCount)
}

fun play(rounds : Int, start1 : Int, start2 : Int, scoreLimit : Int) : Int {
    var deterministicDie = 0
    // this is played on a 0-9 board to make modulos easier
    var player1Pos = start1 - 1
    var player2Pos = start2 - 1
    var score1 = 0
    var score2 = 0
    var countRolls = 0
    for (i in 0 until rounds) {
        val roll1 = roll(deterministicDie)
        val roll2 = roll(roll1)
        val roll3 = roll(roll2)

        player1Pos = (player1Pos + roll1 + roll2 + roll3) % 10
        score1 += player1Pos+1

        countRolls += 3

        if (score1 >= scoreLimit) {
            return score2 * countRolls
        }

        val roll4 = roll(roll3)
        val roll5 = roll(roll4)
        val roll6 = roll(roll5)

        player2Pos = (player2Pos + roll4 + roll5 + roll6) % 10
        score2 += player2Pos+1

        countRolls += 3

        if (score2 >= scoreLimit ) {
            return score1 * countRolls
        }

        deterministicDie = roll6
    }
    return 0
}




fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val data = loadFileAsString(fileName).split("\n").map { it -> it.last().digitToInt() }
        val part1Result = play(1000, data[0], data[1], 1000)
        println("Day 21 part 1 result: $part1Result")
        var cache = mutableMapOf<DiracState, List<Long>>()
        val part2Result = playDiracNaive(1, 0, 0, data[0]-1, data[1]-1, 21, cache).maxOf { it }
        println("Day 21 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}