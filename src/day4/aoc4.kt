package main.day4;

import main.util.loadFileAsString
import java.io.FileNotFoundException

data class BingoResult(val lastCalled : Int, val sumRest : Int)
data class Number(val num: String, var called: Boolean)
data class Board(val data: MutableList<List<Number>>)
data class Bingo(val luckyNumbers: List<String>, var boards: List<Board>)

fun splitData(input: String): Bingo {
    var lineSplit = input.split("\n").filter { x -> !x.trim().isEmpty() }

    var luckyNumbers = lineSplit[0].split(",")
    var boards = lineSplit.drop(1).map { line -> line.split(" ").filter { c -> !c.trim().isEmpty() } }
    var bingoBoards = mutableListOf<Board>()
    var i = 0
    var currentBoard = Board(mutableListOf())
    while (i < boards.size) {
        currentBoard.data.add(boards[i].map { line -> Number(line, false) })
        if (i % 5 == 4) {
            bingoBoards.add(currentBoard)
            currentBoard = Board(mutableListOf())
        }
        i++
    }

    return Bingo(luckyNumbers, bingoBoards.toList())
}

// mark number on board and verify if all members of that column or row are marked
// if yes returns last number and sum of all remaining numbers
// otherwise it returns null
fun markVerifyWon(number: String, board: Board): BingoResult? {
    val allUnmarkedNums = board.data
        .map { x -> x.filter { y -> !y.called }.map { y -> y.num }}
        .flatMap { x -> x.asIterable() }.toSet()

    for (row in board.data) {
        for (colIndex in 0 until row.size) {
            if (row[colIndex].num == number) {
                row[colIndex].called = true

                var fullRow = row
                var fullColumn = (0 until board.data.size)
                    .map { board.data[it][colIndex] }
                if (fullRow.all { el -> el.called }) {
                    return BingoResult(
                        row[colIndex].num.toInt(),
                        allUnmarkedNums
                            .filter { x -> !fullRow.map{ n -> n.num }.contains(x) }
                            .map{x -> x.toInt()}
                            .sumOf { x -> x })
                }
                if (fullColumn.all { el -> el.called }) {
                    return BingoResult(
                        row[colIndex].num.toInt(),
                        allUnmarkedNums
                            .filter { x -> !fullColumn.map { n -> n.num }.contains(x) }
                            .map { x -> x.toInt() }
                            .sumOf { x -> x })
                }
            }
        }
    }
    return null
}

fun findAllWinnerScores(input: String) : List<Int> {
    var winners = mutableListOf<Int>()
    val bingo = splitData(input)
    var candidateBoards = bingo.boards.toMutableList()
    for (num in bingo.luckyNumbers) {
        var foundIndices = mutableListOf<Int>()
        for (boardIndex in 0 until candidateBoards.size) {
            var result = markVerifyWon(num, candidateBoards[boardIndex])
            if (result != null) {
                winners.add(result.lastCalled * result.sumRest)
                foundIndices.add(boardIndex)
            }
        }
        for (idx in foundIndices.sorted().reversed()) {
            candidateBoards.removeAt(idx)
        }
    }
    return winners
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        var allWinners = findAllWinnerScores(loadFileAsString(fileName))
        var part1Marker = allWinners.first()
        var part2Marker = allWinners.last()
        println("Day 4 part 1 result: $part1Marker")
        println("Day 4 part 2 result: $part2Marker")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}