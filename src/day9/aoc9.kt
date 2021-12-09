package main.day9

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException

data class Board(var points : List<List<Int>>) {
    fun at (p: Point) : Int? {
        return points
            .getOrElse(p.y) { listOf() }
            .getOrNull(p.x)
    }
}

fun toBoard(s : String) : Board {
    return Board(s.split("\n").map { it -> it.map { it.digitToInt() } })
}

fun isLowPoint(p : Point, board : Board) : Boolean {
    val height = board.at(p) ?: Int.MAX_VALUE
    return p.neighbours4()
        .map { board.at(it) ?: Int.MAX_VALUE }
        .none { neighbourHeight -> neighbourHeight <= height }
}

fun calculateRiskLevel(s : String) : Int {
    var riskIndex = 0
    val board = toBoard(s)
    for (rowIdx in board.points.indices) {
        val row = board.points[rowIdx]
        for (colIdx in row.indices) {
            if (isLowPoint(Point(colIdx, rowIdx), board)) {
                riskIndex += row[colIdx] + 1
            }
        }
    }
    return riskIndex
}

fun discoverBasinSize(p : Point, b : Board) : Int {
    var basin = mutableSetOf(p)
    var candidates = mutableListOf(p)
    while (candidates.isNotEmpty()) {
        val candidate = candidates.removeFirst()
        basin.add(candidate)
        val candidateHeight = b.at(candidate) ?: Int.MAX_VALUE
        val neighbours = candidate
            .neighbours4()
            .filter { !basin.contains(it) }
            .filter { b.at(it) != null }
            .filter { (b.at(it) ?: Int.MAX_VALUE) >= candidateHeight }
            .filter { (b.at(it) ?: Int.MAX_VALUE) < 9 }
        candidates.addAll(neighbours)
    }
    return basin.size
}

fun findAllBasins(s : String) : List<Int> {
    var basins = mutableListOf<Int>()
    val board = toBoard(s)
    for (rowIdx in board.points.indices) {
        val row = board.points[rowIdx]
        for (colIdx in row.indices) {
            if (isLowPoint(Point(colIdx, rowIdx), board)) {
                // find size of basin
                basins.add(discoverBasinSize(Point(colIdx, rowIdx), board))
            }
        }
    }
    return basins
}

fun findBasinsFactor(s : String) : Long {
    return findAllBasins(s).sorted().reversed().take(3).fold(1) { x, y -> x * y }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = calculateRiskLevel(loadFileAsString(fileName))
        val part2Result = findBasinsFactor(loadFileAsString(fileName))
        println("Day 9 part 1 result: $part1Result")
        println("Day 9 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}