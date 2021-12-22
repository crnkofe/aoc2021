package main.day22

import main.util.Point3
import main.util.loadFileAsString
import java.io.FileNotFoundException
import kotlin.math.max
import kotlin.math.min

data class Cube(val cmd : Boolean, val p1 : Point3, val p2 : Point3)

fun arseInut(s: String) : List<Cube> {
    var parserRegex = "(on|off) x=([-]?[0-9]+)..([-]?[0-9]+),y=([-]?[0-9]+)..([-]?[0-9]+),z=([-]?[0-9]+)..([-]?[0-9]+)".toRegex()
    return s.split("\n")
        .mapNotNull { parserRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .map { Cube(it[1] == "on",
            Point3(it[2].toInt(), it[4].toInt(), it[6].toInt()),
            Point3(it[3].toInt(), it[5].toInt(), it[7].toInt())) }
}

// paint takes commands that either turn on/off a switch in a cube region
// this version ignores everything outside of initial region
fun paint(cmds : List<Cube>) : Int {
    val pimn = Point3(-50,-50,-50)
    val pimx = Point3(50,50,50)

    var lit = mutableMapOf<Point3, Boolean>()
    for (c in cmds) {
        for (x in max(c.p1.x, pimn.x)..min(c.p2.x, pimx.x)) {
            for (y in max(c.p1.y, pimn.y)..min(c.p2.y, pimx.y)) {
                for (z in max(c.p1.z, pimn.z)..min(c.p2.z, pimx.z)) {
                    if (c.cmd) {
                        lit[Point3(x, y, z)] = true
                    } else {
                        lit.remove(Point3(x, y, z))
                    }
                }
            }
        }
    }
    return lit.values.size
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val parsedInput = arseInut(loadFileAsString(fileName))
        val part1Result = paint(parsedInput)
        println("Day 22 part 1 result: $part1Result")
        val part2Result = 0
        println("Day 22 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}