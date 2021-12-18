package main.day17

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.lang.Integer.max
import java.lang.Integer.min

data class TargetArea(val bottomLeft : Point, val topRight : Point)

data class Step(val step : Int, var x : Int, var vx : Int, val vxStart : Int, val yMax : Int, var vyStart : Int)

fun parseInput (s : String) : TargetArea {
    val parserRegex = "target area: x=([-]?[0-9]+)..([-]?[0-9]+), y=([-]?[0-9]+)..([-]?[0-9]+)".toRegex()
    val data = parserRegex.matchEntire(s)?.groupValues?.map { it }?.toList() ?: listOf()
    return TargetArea(
        Point((data.getOrNull(1) ?: "0").toInt(), (data.getOrNull(3) ?: "0").toInt()),
        Point((data.getOrNull(2) ?: "0").toInt(), (data.getOrNull(4) ?: "0").toInt()))
}

fun simulateFindValidY(step : Step, ta : TargetArea, maxY : Int, additionalSteps : Int) : Set<Step> {
    val minY = min(ta.bottomLeft.y, ta.topRight.y)
    var resultY = mutableSetOf<Step>()
    for (vy in minY..maxY) {
        var y = 0
        var currentVY = vy
        var maxCurrentY = 0

        var sx = 0
        var stepVX = step.vxStart
        // calculate y at step step
        for (s in (0 until step.step + additionalSteps)) {
            // once x velocity reaches 0 y velocity can still fall
            if ((s >= step.step) && isXValid(sx, ta) && isYValid(y, ta)) {
                resultY.add(Step(s, sx, step.vx, step.vxStart, maxCurrentY, vy))
            }

            y += currentVY
            currentVY--
            maxCurrentY = max(maxCurrentY, y)

            // we also need to recalculate X in case it goes out of bounds
            sx += stepVX
            if (stepVX != 0) {
                stepVX = if (stepVX > 0) stepVX-1 else stepVX+1
            }
        }
    }
    return resultY
}


fun simulateY(step : Step, ta : TargetArea, maxY : Int, additionalSteps : Int) : Set<Step> {
    val minY = min(ta.bottomLeft.y, ta.topRight.y)
    var resultY = mutableSetOf<Step>()
    for (vy in minY..maxY) {
        var y = 0
        var currentVY = vy
        var maxCurrentY = 0

        var sx = 0
        var stepVX = step.vxStart
        // calculate y at step step
        for (s in (0 until step.step + additionalSteps)) {
            y += currentVY
            currentVY--
            maxCurrentY = max(maxCurrentY, y)

            // we also need to recalculate X in case it goes out of bounds
            sx += stepVX
            if (stepVX != 0) {
                stepVX = if (stepVX > 0) stepVX-1 else stepVX+1
            }

            // once x velocity reaches 0 y velocity can still fall
            if ((s >= step.step) && (isYValid(y, ta))) {
                resultY.add(Step(maxCurrentY, sx, step.vx, step.vxStart, maxCurrentY, vy))
            }
        }
    }
    return resultY
}

fun isXValid(x : Int, ta : TargetArea) : Boolean {
    return (x >= ta.bottomLeft.x) && (x <= ta.topRight.x) ||
            (x <= ta.bottomLeft.x) && (x >= ta.topRight.x)
}

fun isYValid(y : Int, ta : TargetArea) : Boolean {
    return (y >= ta.bottomLeft.y) && (y <= ta.topRight.y) ||
            (y <= ta.bottomLeft.y) && (y >= ta.topRight.y)
}

fun findValidXs(ta : TargetArea) : Set<Step> {
    val minX = min(ta.bottomLeft.x, ta.topRight.x)

    var validSteps = mutableSetOf<Step>()
    for (startVX in (if (minX > 0) 1 else -1)..max(ta.bottomLeft.x, ta.topRight.x)) {
        var startX = 0
        var vx = startVX
        var step = 1
        while (vx != 0) {
            startX += vx
            vx = if (vx > 0) vx-1 else vx+1
            if (isXValid(startX, ta)) {
                validSteps.add(Step(step, startX, vx, startVX, 0, 0))
            }
            step++
        }
    }
    return validSteps
}

fun findValidXs(s : String) : Int {
    val targetArea = parseInput(s)
    val steps = findValidXs(targetArea)

    val ys = steps.map { simulateY(it, targetArea, 500, 800) }.flatten()
    return ys.maxOf { it.yMax }
}

fun findAllStartingVelocities(s : String) : Int {
    val targetArea = parseInput(s)
    val steps = findValidXs(targetArea)

    var velocities = mutableSetOf<Point>()
    for (step in steps) {
        val options = simulateFindValidY(step, targetArea, 500, 800)
        for (sy in options) {
            velocities.add(Point(sy.vxStart, sy.vyStart))
        }
    }
    return velocities.size
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = findValidXs(loadFileAsString(fileName))
        println("Day 17 part 1 result: $part1Result")
        val part2Result = findAllStartingVelocities(loadFileAsString(fileName))
        println("Day 17 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}