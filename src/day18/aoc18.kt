package main.day18

import main.util.loadFileAsString
import java.io.FileNotFoundException
import java.lang.Integer.max
import java.util.*
import kotlin.math.exp

data class ReduceOp(val n : SnailNumber, val protected : Boolean)

open class SnailNumber {
    open fun add(n: SnailNumber) {
        throw NotImplementedError()
    }

    open fun increase(n: Int) {

    }

    open fun set(n: Int) {

    }

    open fun magnitude(): Int {
        return 0
    }

    open fun  extractNumbers(): MutableList<Number> {
        return mutableListOf()
    }

    open fun copy(): SnailNumber {
        return this
    }

    open fun value(): Int? {
        return null
    }

    open fun reduceDepth(depth: Int, explodeOnly : Boolean): List<SnailNumber> {
        throw NotImplementedError()
    }

    open fun reduce(depth: Int, allNumbers: MutableList<Number>, explodeOnly: Boolean): ReduceOp {
        throw NotImplementedError()
    }
}

data class Number(var n: Int, var id: UUID) : SnailNumber() {
    override fun add(n: SnailNumber) {
        throw NotImplementedError()
    }

    override fun value(): Int? {
        return n
    }

    override fun increase(a: Int) {
        this.n += a
    }

    override fun set(n: Int) {
        this.n = n
    }

    override fun magnitude(): Int {
        return this.n
    }

    override fun extractNumbers(): MutableList<Number> {
        return mutableListOf(this)
    }

    override fun copy(): SnailNumber {
        return Number(this.n, this.id)
    }

    override fun reduceDepth(depth: Int, explodeOnly: Boolean): List<Number> {
        return listOf()
    }

    override fun reduce(depth: Int, allNumbers: MutableList<Number>, explodeOnly: Boolean): ReduceOp {
        if (explodeOnly) {
            return ReduceOp(this, false)
        }

        if (n >= 10) {
            val first = Number(n / 2, UUID.randomUUID())
            val second = Number(n / 2 + n % 2, UUID.randomUUID())
            allNumbers.add(allNumbers.indexOf(this)+1, second)
            allNumbers[allNumbers.indexOf(this)] = first
            return ReduceOp(SnailStack(mutableListOf(first, second)), true)
        } else {
            return ReduceOp(this, false)
        }
    }

    override fun toString(): String {
        return this.n.toString()
    }
}

class SnailStack(var numbers: MutableList<SnailNumber>) : SnailNumber() {
    override fun add(n: SnailNumber) {
        this.numbers.add(n)
    }

    override fun extractNumbers(): MutableList<Number> {
        return this.numbers.map { it.extractNumbers() }.flatten().toMutableList()
    }

    override fun magnitude(): Int {
        return 3 * this.numbers.first().magnitude() + 2 * this.numbers.last().magnitude()
    }

    override fun reduceDepth(depth: Int, explodeOnly: Boolean): List<SnailNumber> {
        if (!explodeOnly) {
            return emptyList()
        }
        if (depth < 4) {
            return listOf()
        }

        if (this.numbers.size == 2) {
            val v1 = this.numbers[0]
            val v2 = this.numbers.last()
            if ((v1 != null) && (v2 != null) && (v1.value() != null) && (v2.value() != null)) {
                return listOf(v1, v2)
            }
        }

        return listOf()
    }

    override fun reduce(depth: Int, allNumbers: MutableList<Number>, explodeOnly: Boolean): ReduceOp {
        var previousN: SnailNumber? = null
        return this.reduceDepth(depth, allNumbers, explodeOnly)
    }

    private fun reduceDepth(
        depth: Int,
        allNumbers: MutableList<Number>,
        explodeOnly: Boolean
    ): ReduceOp {
        var originalS = this.toString()
        // if any split occurs protect any other element from being manipulated with
        var protected = false
        var n = 0
        while (n < this.numbers.size) {
            if (protected) {
                break
            }
            if (depth >= 4) {
                val splitN = numbers[n].reduceDepth(depth + 1, explodeOnly)
                if (splitN.isNotEmpty()) {
                    val lastIndex: Int = allNumbers.indexOf(splitN.last()) + 1
                    val zeroIndex = allNumbers.indexOf(splitN.first())
                    val previousIndex: Int = allNumbers.indexOf(splitN.first()) - 1

                    if (lastIndex < allNumbers.size) {
                        allNumbers[lastIndex].increase(splitN.last().value() ?: 0)
                    }

                    if (previousIndex >= 0) {
                        allNumbers[previousIndex].increase(splitN.first().value() ?: 0)
                    }

                    if (zeroIndex != -1) {
                        val zeroNumber = Number(0, UUID.randomUUID())
                        this.numbers[n] = zeroNumber
                        allNumbers[zeroIndex] = zeroNumber
                        allNumbers.removeAt(zeroIndex + 1)
                    } else {
                        throw Exception("impossible")
                    }

                    n = 0
                    continue
                }
            }

            val previousNS = this.numbers[n].toString()
            val replaceWith = this.numbers[n].reduce(depth + 1, allNumbers, explodeOnly)
            if (replaceWith.n.toString() != previousNS) {
                this.numbers[n] = replaceWith.n
                if (replaceWith.protected) {
                    protected = true
                    break
                }
                n = 0
                continue
            }
            n++
        }
        return ReduceOp(this, protected)
    }

    override fun toString(): String {
        return "[" + this.numbers.map { it.toString() }.joinToString(",") + "]"
    }
}

fun parseInput(s: String): SnailNumber {
    var stack = mutableListOf<SnailNumber>()
    var currentNumber = ""
    for (c in s) {
        when (c) {
            '[' -> stack.add(SnailStack(mutableListOf()))
            ',' -> {
                if (currentNumber != "") {
                    stack.last().add(Number(currentNumber.toInt(), UUID.randomUUID()))
                    currentNumber = ""
                }
            }
            ']' -> {
                if (currentNumber != "") {
                    stack.last().add(Number(currentNumber.toInt(), UUID.randomUUID()))
                    currentNumber = ""
                }
                val newStack = stack.removeLast()
                if (stack.isEmpty()) {
                    return newStack
                }
                stack.last().add(newStack)
            }
            else -> {
                currentNumber += c
            }
        }
    }
    return stack.first()
}

fun evaluateSplit(s : String, splitOnly: Boolean) : SnailNumber {
    var parsedInput = parseInput(s)
    var newParsedInput = parseInput(s)
    var changed = true
    while (changed) {
        var nums = newParsedInput.extractNumbers()
        newParsedInput = newParsedInput.reduce(1, nums, true).n
        nums = newParsedInput.extractNumbers()
        newParsedInput = newParsedInput.reduce(1, nums, false).n
        changed = newParsedInput.toString() != parsedInput.toString()
        parsedInput = parseInput(newParsedInput.toString())
    }
    return newParsedInput
}

fun evaluate(s: String): String {
    var newParsedInput = parseInput(s)
    var explodedSnailNumber = evaluateSplit(newParsedInput.toString(), true)
    newParsedInput = evaluateSplit(explodedSnailNumber.toString(), false)
    return newParsedInput.toString()
}

fun evaluateFile(s: String): Int {
    var expression = ""
    for (line in s.split("\n")) {
        if (expression != "") {
            expression = evaluate("[$expression,$line]")
        } else {
            expression = line
        }
    }
    return evaluateMag(expression)
}

fun evaluateFileForMaxExpression(s: String): Int {
    var maxMag = 0
    for (line1 in s.split("\n")) {
        for (line2 in s.split("\n")) {
            maxMag = max(maxMag, evaluateMag(evaluate("[$line1,$line2]")))
            maxMag = max(maxMag, evaluateMag(evaluate("[$line2,$line1]")))
        }
    }
    return maxMag
}


fun evaluateMag(s: String): Int {
    var parsedInput = parseInput(s)
    return parsedInput.magnitude()
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = evaluateFile(loadFileAsString(fileName))
        println("Day 18 part 1 result: $part1Result")
        val part2Result = evaluateFileForMaxExpression(loadFileAsString(fileName))
        println("Day 18 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}