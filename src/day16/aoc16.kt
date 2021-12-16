package main.day16

import main.util.loadFileAsString
import java.io.FileNotFoundException

data class Packet(val version : Int,
                  val packetID : Long,
                  val literal : Long,
                  val subpackets : List<Packet>,
                  val lastIndex : Int)

data class LiteralResult(val literal : Long, val lastIndex : Int)

fun toBinaryPadded(i : Int) : String {
    var s = i.toString(2)
    while (s.length < 4) {
        s = "0$s"
    }
    return s
}

fun parsePacket(input : String, startIndex : Int) : Packet {
    val literalPacket = 4.toLong()
    val fullS =  input.map { toBinaryPadded(it.digitToInt(16)) }.joinToString("")
    val s = fullS.drop(startIndex)
    val version = s.take(3).toInt(2)
    val packetID = s.drop(3).take(3).toLong(2)
    var literal : Long = 0
    var lastIndex = startIndex

    var subpackets = mutableListOf<Packet>()

    if (packetID == literalPacket) {
        val literalResult = parseLiteral(s, startIndex)
        literal = literalResult.literal
        lastIndex = literalResult.lastIndex + 1
    } else {
        val typeID = s.drop(6).take(1).toInt(2)
        if (typeID == 0) {
            val totalLengthInBits = s.drop(7).take(15).toInt(2)
            val startingIndex = startIndex + 7 + 15
            var currentIdx = startingIndex
            while ((currentIdx - startingIndex) < totalLengthInBits) {
                val newPacket = parsePacket(input, currentIdx)
                subpackets.add(newPacket)
                currentIdx = newPacket.lastIndex
            }
            lastIndex = currentIdx
        } else {
            val numberOfSubpackets = s.drop(7).take(11).toInt(2)
            lastIndex = startIndex + 7 + 11
            for (i in 0 until numberOfSubpackets) {
                val newPacket = parsePacket(input, lastIndex)
                subpackets.add(newPacket)
                lastIndex = newPacket.lastIndex
            }
        }
    }
    return Packet(version, packetID, literal, subpackets, lastIndex)
}

private fun parseLiteral(s: String, start : Int) : LiteralResult {
    var index = 6
    var rest = s.drop(6)
    var cc = ""
    while (rest.isNotEmpty()) {
        val w = rest.take(5)
        cc += w.drop(1).take(4)
        index += 5
        if (w.first() == '0') {
            break
        }
        rest = rest.drop(5)
    }
    return LiteralResult(cc.toLong(2), start + index - 1)
}

fun sumVersions(p : Packet) : Int {
    return p.version + p.subpackets.map { sumVersions(it) }.sum()
}

fun evaluateExpression(p : Packet) : Long {
    return when (p.packetID.toInt()) {
        0 -> p.subpackets.sumOf { evaluateExpression(it) }
        1 -> p.subpackets.fold(1) { acc, it -> acc * evaluateExpression(it) }
        2 -> p.subpackets.map { evaluateExpression(it) }.minOf { it }
        3 -> p.subpackets.map { evaluateExpression(it) }.maxOf { it }
        4 -> p.literal
        5 -> if (evaluateExpression(p.subpackets.first()) > evaluateExpression(p.subpackets.last())) 1 else 0
        6 -> if (evaluateExpression(p.subpackets.first()) < evaluateExpression(p.subpackets.last())) 1 else 0
        7 -> if (evaluateExpression(p.subpackets.first()) == evaluateExpression(p.subpackets.last())) 1 else 0
        else -> 0
    }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val p = parsePacket(loadFileAsString(fileName),0)
        val part1Result = sumVersions(p) // findShortestPath(Point(0, 0), goal, board)
        println("Day 16 part 1 result: $part1Result")
        val part2Result = evaluateExpression(p)
        println("Day 16 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}
