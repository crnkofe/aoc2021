package main.day21

import main.util.loadFileAsString
import java.io.FileNotFoundException

fun roll(previous : Int) : Int{
    return previous+1
}

fun play(rounds : Int, start1 : Int, start2 : Int) : Int {
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

        if (score1 >= 1000) {
            return score2 * countRolls
        }

        val roll4 = roll(roll3)
        val roll5 = roll(roll4)
        val roll6 = roll(roll5)

        player2Pos = (player2Pos + roll4 + roll5 + roll6) % 10
        score2 += player2Pos+1

        countRolls += 3

        if (score2 >= 1000 ) {
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

        val part1Result = play(1000, data[0], data[1])
        println("Day 21 part 1 result: $part1Result")
        val part2Result = 0
        println("Day 21 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}