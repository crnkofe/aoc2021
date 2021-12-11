package main.day11

import main.util.Board
import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException

private fun checkBlink(
    board: Board,
    p: Point,
    blinkedSet: MutableSet<Point>,
    neufPoint: MutableList<Point>
) : Int {
    var blinkedThisRound = 0
    if ((board.inc(p, 1) ?: 0) > 9) {
        blinkedThisRound += 1
        board.set(p, 0)
        for (n in p.neighbours8().filter { !blinkedSet.contains(it) }) {
            neufPoint.add(n)
        }
        blinkedSet.add(p)
    }
    return blinkedThisRound
}

data class StepsEnlightened(val blinked : Int, val allBlinkedSteps : List<Int>)

fun simulateEnlightened(s : String, steps : Int) : StepsEnlightened {
    var board = Board(s.split("\n")
        .map { it.toCharArray() }
        .map { it.map { c -> c.digitToInt() }.toMutableList() }
        .toMutableList()
    )

    val boardSize = board.points.size * board.points[0].size

    var blinked = 0
    var allBlinkedStep = mutableListOf<Int>()

    for (i in 0 until steps) {
        var blinkedThisRound = 0
        // octopuses that have already blinked this round
        var blinkedSet = mutableSetOf<Point>()
        // candidates that can still blink
        var neufPoint = mutableListOf<Point>()
        for (p in board.points()) {
            blinkedThisRound += checkBlink(board, p, blinkedSet, neufPoint)
        }

        while (neufPoint.isNotEmpty()) {
            neufPoint.removeAll(blinkedSet)
            if (neufPoint.isEmpty()) {
                break
            }
            val p = neufPoint.removeFirst()
            blinkedThisRound += checkBlink(board, p, blinkedSet, neufPoint)
        }
        blinked += blinkedThisRound
        if (blinkedThisRound == boardSize) {
            // only remember first time anything blinks
            allBlinkedStep.add(i + 1)
        }
    }
    return StepsEnlightened(blinked, allBlinkedStep)
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = simulateEnlightened(loadFileAsString(fileName), 100).blinked
        val part2Result = simulateEnlightened(loadFileAsString(fileName), 1000).allBlinkedSteps.first()
        println("Day 11 part 1 result: $part1Result")
        println("Day 11 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}