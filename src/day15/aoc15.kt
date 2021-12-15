package main.day15

import main.util.Board
import main.util.Point
import main.util.loadFileAsString
import main.util.manhattan
import java.io.FileNotFoundException

fun parseRiskBoard(s : String) : Board =
    Board(s.split("\n")
        .map { it.map { c -> c.digitToInt() }.toMutableList() }
        .toMutableList())

fun convertPointValue(m : Int, v : Int) : Int {
    var newV = m + v
    while (newV > 9) {
        newV -= 9
    }
    return newV
}

fun multiplyBoard(b : Board, times : Int) : Board {
    val w = b.points[0].size
    val h = b.points.size

    var lines = mutableListOf<MutableList<Int>>()
    for (y in 0 until (h * times)) {
        lines.add((0 until (w * times))
            .map {  convertPointValue(it.div(w) + y.div(h), b.at(Point(it % w, y % h)) ?: throw Exception("fail")) }
            .toMutableList())
    }
    return Board(lines)
}

fun findShortestPath(start: Point, end: Point, board: Board) : Int {
    var heap = mutableListOf(start)

    var cameFrom = mutableMapOf<Point, Point>()

    var gScore = mutableMapOf(start to 0)
    var fScore = mutableMapOf(start to manhattan(start, end))

    while (heap.isNotEmpty()) {
        val current = heap.removeFirst()
        if (current == end) {
            var x = current
            var cst = 0
            var path = listOf(x)
            while (x != start) {
                cst += board.at(x) ?: 0
                x = cameFrom[x] ?: throw Exception("fail")
                path = listOf(x).plus(path)
            }
            return cst
        }

        for (n in current.neighbours4().filter { board.at(it) != null }) {
            val tentNScore = (gScore[current] ?: 0) + (board.at(n) ?: Int.MAX_VALUE)
            if (tentNScore < (gScore[n] ?: Int.MAX_VALUE)) {
                cameFrom[n] = current
                gScore[n] = tentNScore
                fScore[n] = tentNScore + manhattan(n, end)
                if (!heap.contains(n)) {
                    heap.add(n)
                    heap = heap.sortedBy { fScore.getOrDefault(it, Int.MAX_VALUE) }.toMutableList()
                }
            }
        }
    }

    return 0
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val board = parseRiskBoard(loadFileAsString(fileName))
        val boardTimes5 = multiplyBoard(board, 5)
        val goal = Point(board.points[0].size - 1, board.points.size - 1)
        val goal5 = Point(boardTimes5.points[0].size - 1, boardTimes5.points.size - 1)
        val part1Result = findShortestPath(Point(0, 0), goal, board)
        println("Day 15 part 1 result: $part1Result")
        val part2Result = findShortestPath(Point(0, 0), goal5, boardTimes5)
        println("Day 15 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}
