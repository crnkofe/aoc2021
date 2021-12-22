package main.day22

import main.util.Point
import main.util.Point3
import main.util.loadFileAsString
import java.io.FileNotFoundException
import kotlin.math.max
import kotlin.math.min

data class Cube(val cmd: Boolean, val p1: Point3, val p2: Point3) {
    fun contains(other: Cube): Boolean {
        return (p1.x <= other.p1.x) &&
                (p1.y <= other.p1.y) &&
                (p1.z <= other.p1.z) &&
                (p2.x >= other.p2.x) &&
                (p2.y >= other.p2.y) &&
                (p2.z >= other.p2.z)
    }

    fun containsPoint3(p: Point3): Boolean {
        return ((p.x >= p1.x) && (p.y >= p1.y) && (p.z >= p1.z) &&
                (p.x <= p2.x) && (p.y <= p2.y) && (p.z <= p2.z))
    }

    // algorithm idea:
    // add first cube to list
    // for each subsequent cube
    // intersect all existing cubes with current cube
    // remove all cubes that are contained within current cube
    // at the end sum up all on cubes

    // intersect this cube with all planes of other cube
    // in case there is no intersection nothing is returned
    // else at most one intersection will be made
    fun intersectOnceWith(other: Cube): List<Cube> {
//        if (!containsPoint3(other.p1) && !containsPoint3(other.p2) && !other.containsPoint3(p1) && !other.containsPoint3(p2)) {
//            return listOf()
//        }

        var res1 = intersectWithPoint(other.p1)
        if (res1.isEmpty()) {
            return intersectWithPoint(other.p2)
        }
        return res1
    }

    fun intersectWithPoint(p: Point3): List<Cube> {
        if ((p1.x < p.x) && (p2.x >= p.x)) {
            return listOf(
                Cube(this.cmd, p1, Point3(p.x - 1, p2.y, p2.z)),
                Cube(this.cmd, Point3(p.x, p1.y, p1.z), p2),
            )
        }
        if ((p1.y < p.y) && (p2.y >= p.y)) {
            return listOf(
                Cube(this.cmd, p1, Point3(p2.x, p.y - 1, p2.z)),
                Cube(this.cmd, Point3(p1.x, p.y, p1.z), p2),
            )
        }
        if ((p1.z < p.z) && (p2.z >= p.z)) {
            return listOf(
                Cube(this.cmd, p1, Point3(p2.x, p2.y, p.z - 1)),
                Cube(this.cmd, Point3(p1.x, p1.y, p.z), p2),
            )
        }
        return listOf()
    }
}

fun parseInput(s: String): List<Cube> {
    var parserRegex =
        "(on|off) x=([-]?[0-9]+)..([-]?[0-9]+),y=([-]?[0-9]+)..([-]?[0-9]+),z=([-]?[0-9]+)..([-]?[0-9]+)".toRegex()
    return s.split("\n")
        .mapNotNull { parserRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .map {
            Cube(
                it[1] == "on",
                Point3(it[2].toInt(), it[4].toInt(), it[6].toInt()),
                Point3(it[3].toInt(), it[5].toInt(), it[7].toInt())
            )
        }
}

// paint takes commands that either turn on/off a switch in a cube region
// this version ignores everything outside of initial region
fun paint(cmds: List<Cube>): Int {
    val pimn = Point3(-50, -50, -50)
    val pimx = Point3(50, 50, 50)

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

fun countSwitchedOn(cmds: List<Cube>): Long {
    var cubes = mutableListOf<Cube>()

    for (cmd in cmds) {
        println("at cmd $cmd: " + cubes
            .filter { it.cmd }
            .map { ((it.p2.x.toLong() - it.p1.x.toLong() +1) * (it.p2.y.toLong() - it.p1.y.toLong()+1) * (it.p2.z.toLong() - it.p1.z.toLong() + 1)).toLong() }
            .sumOf { it }.toString())

        var newCubes = mutableListOf<Cube>()
        while (cubes.isNotEmpty()) {
            var existingCube = cubes.removeFirst()
            var intersections = existingCube.intersectOnceWith(cmd)
                .filter { !cmd.contains(it) }
            if (intersections.isEmpty()) {
                // no intersection with current cube
                newCubes.add(existingCube)
            } else {
                // split cube and add it to cubes for processing (these cubes can still further be intersected
                cubes.addAll(0, intersections)
            }
        }
        cubes = newCubes.filter { !cmd.contains(it) }.plus(cmd).toMutableList()
    }

    return cubes
        .filter { it.cmd }
        .map { ((it.p2.x.toLong() - it.p1.x.toLong() +1) * (it.p2.y.toLong() - it.p1.y.toLong()+1) * (it.p2.z.toLong() - it.p1.z.toLong() + 1)).toLong() }
        .sumOf { it }
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val parsedInput = parseInput(loadFileAsString(fileName))
        val part1Result = paint(parsedInput)
        println("Day 22 part 1 result: $part1Result")
        val part2Result = 0
        println("Day 22 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}