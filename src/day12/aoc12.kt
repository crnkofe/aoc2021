package main.day12

import main.util.loadFileAsString
import java.io.FileNotFoundException


data class Path(val path: List<String>) {
    fun prepend(node : String) : Path {
           return Path(listOf(node).plus(this.path))
    }
}

data class Graph(val graph : Map<String, List<String>>) {
    private fun isSmallCave(s : String) : Boolean {
        var parserRegex = "[a-z]+?".toRegex()
        return parserRegex.matches(s)
    }

    private fun neighbours(node : String) : List<String> {
        val setA : Set<String> = graph.filterKeys { it == node }.values.flatten().toSet()
        val setB = graph.filterValues { it.contains(node) }.keys
        return setA.union(setB).toList()
    }

    fun dfsSmallCaves(node : String, depth: Int, visitedSmallCaves : Set<String>) : List<Path> {
        if (node == "end") {
            return listOf(Path(listOf( node)))
        }

        var paths = mutableListOf<Path>()

        val neighbourNodes = neighbours(node)
        for (neighbour in neighbourNodes.filter { !visitedSmallCaves.contains(it) }) {
            val newVisited = if (isSmallCave(neighbour)) visitedSmallCaves.plus(neighbour) else visitedSmallCaves
            paths.addAll(dfsSmallCaves(neighbour, depth+1, newVisited).map { it.prepend(node) })
        }

        return paths
    }

    fun dfsSmallCavesRepeat(node : String, depth: Int, visitedSmallCaves : Map<String, Int>) : List<Path> {
        if (node == "end") {
            return listOf(Path(listOf( node)))
        }

        var paths = mutableListOf<Path>()

        val neighbourNodes = neighbours(node)
        for (neighbour in neighbourNodes) {
            if (neighbour == "start") {
                continue
            }
            val countVisited = visitedSmallCaves.getOrDefault(neighbour, 0)
            if (isSmallCave(neighbour)) {
                if (countVisited == 2) {
                    continue
                } else if (countVisited == 1) {
                    if (visitedSmallCaves
                        .filterKeys { it != neighbour }
                            .filterKeys { isSmallCave(it) }
                        .filterValues { it == 2 }
                        .any()) {
                        continue
                    }
                }
            }

            paths.addAll(dfsSmallCavesRepeat(neighbour, depth+1,
                visitedSmallCaves.plus(neighbour to countVisited+1)).map { it.prepend(node) })
        }

        return paths
    }
}

fun findAllPaths(s : String) : Int {
    var parserRegex = "([a-zA-Z]*)[-]([a-zA-Z]*)".toRegex()
    val graph = Graph(s.split("\n")
        .mapNotNull { parserRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .groupBy({ it[1] }, { it[2] }))

    return graph.dfsSmallCaves("start", 0, setOf("start")).size
}

fun findAllPathsWithRepeat(s : String) : Int {
    var parserRegex = "([a-zA-Z]*)[-]([a-zA-Z]*)".toRegex()
    val graph = Graph(s.split("\n")
        .mapNotNull { parserRegex.matchEntire(it) }
        .map { match -> match.groupValues.toList() }
        .groupBy({ it[1] }, { it[2] }))

    val paths = graph.dfsSmallCavesRepeat("start", 0, mapOf("start" to 1))
    return paths.size
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = findAllPaths(loadFileAsString(fileName))
        val part2Result = findAllPathsWithRepeat(loadFileAsString(fileName))
        println("Day 12 part 1 result: $part1Result")
        println("Day 12 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}