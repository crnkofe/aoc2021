package main.day25

import main.util.Point
import main.util.loadFileAsString
import java.io.FileNotFoundException

data class CucumberMap(var cucumbers : MutableList<Cucumber>, val width : Int, val height: Int) {
    fun print() {
        for (y in 0 until height) {
            var line = ""
            for (x in 0 until width) {
                val matchinguCucumbers = cucumbers.filter { it.pos == Point(x, y) }
                if (matchinguCucumbers.isEmpty()) {
                    line += '.'
                } else {
                    line += if (matchinguCucumbers.first().directionX != 0) '>' else 'v'
                }
            }
            println(line)
        }
        println("")
    }
}



data class Cucumber(val directionX : Int, val directionY : Int, val pos : Point)

fun simulate(map : CucumberMap) : Int {
    var oldmap = map.copy()
    var changed = true
    var step = 0
    //oldmap.print()
    while (changed){
        changed = false
       var newmap = CucumberMap(mutableListOf(), oldmap.width, oldmap.height)
       for (cucumber in oldmap.cucumbers) {
           if (cucumber.directionX == 0) {
               continue
           }
           var newPos = cucumber.pos + Point(cucumber.directionX, cucumber.directionY)
           newPos.x = newPos.x % oldmap.width

           if  (oldmap.cucumbers.none { it.pos == newPos }) {
               newmap.cucumbers.add(Cucumber(cucumber.directionX, cucumber.directionY, newPos))
               changed = true
           } else {
               newmap.cucumbers.add(cucumber)
           }
       }

        for (cucumber in oldmap.cucumbers) {
            if (cucumber.directionY == 0) {
                continue
            }
            var newPos = cucumber.pos + Point(cucumber.directionX, cucumber.directionY)
            newPos.y = newPos.y % oldmap.height

            if  ((oldmap.cucumbers.filter { it.directionY != 0 }.none { it.pos == newPos }) &&
                 (newmap.cucumbers.none { it.pos == newPos })) {
                newmap.cucumbers.add(Cucumber(cucumber.directionX, cucumber.directionY, newPos))
                changed = true
            } else {
                newmap.cucumbers.add(cucumber)
            }
        }

        // println("step $step")
        // newmap.print()
        oldmap = newmap
        step++
    }
    return step
}

fun parseCucumberMap(s : String) : CucumberMap {
    var cucumbers = mutableListOf<Cucumber>()

    var height = 0
    var width = 0
    for (iline in s.split("\n").withIndex()) {
        height += 1
        width = iline.value.length
        for (ic in iline.value.withIndex()) {
            cucumbers.add(when (ic.value) {
                '>' -> Cucumber(1, 0, Point(ic.index, iline.index))
                'v' -> Cucumber(0, 1, Point(ic.index, iline.index))
                else -> continue
            })
        }
    }

    return CucumberMap(cucumbers, width, height)
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val cmap = parseCucumberMap(loadFileAsString(fileName))
        val part1Result = simulate(cmap)
        println("Day 25 part 1 result: $part1Result")
        val part2Result = 0
        println("Day 25 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}