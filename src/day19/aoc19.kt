package main.day19

import main.util.loadFileAsString
import org.apache.commons.math3.geometry.euclidean.threed.Rotation
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import java.io.FileNotFoundException
import kotlin.math.PI
import kotlin.math.roundToInt


/**
 * Algorithm approach:
 *
 * Load data per scanner
 * Each scanner sees a "default" set of beacons
 *
 * TODO: create matrix multiplication and rotation matrix generation lib
 *
 * For each scanner:
 *  rotate cloud 3 times by 90 degrees across each of axis (x, y, z)
 *  do set intersection with all possible point clouds of other beacons
 *  if 12 points match remove all invalid beacon clouds from both scanners
 *
 * This idea doesn't work. We don't know where exactly scanner is. Point don't really intersect if I attempt set intersection.
 *
 * For each scanner:
 *   For each point:
 *     Calculate vector to each other point (make it a set)
 *
 *
 * After the loop take the union of all remaining sets of beacon clouds (should be one per scanner)
 * We want each scanner position and orientation (set of valid beacon measurements.
 */

data class Scanner(val name : String, val original : BeaconCloud, var beaconClouds : List<BeaconCloud>) {

}
data class BeaconCloud(val rotation : Rotation, val points : MutableList<Vector3D>) {
    fun addVec(v : Vector3D) {
        val pointSize = points.size
        for (i in 0 until pointSize) {
            points[i] = points[i].add(v)
        }
    }
}

fun parseScanners(s : String) : List<Scanner> {
    var nameRegex = "--- ([a-zA-Z0-9 ]+) ---".toRegex()
    var vecRegex = "([-]?[0-9]+),([-]?[0-9]+),([-]?[0-9]+)".toRegex()

    var scanners = mutableListOf<Scanner>()

    var name = ""
    var currentPoints = mutableSetOf<Vector3D>()
    for (line in s.split("\n").filter { it != "" } ) {
        val match = nameRegex.matchEntire(line)
        if (match != null) {
            if (currentPoints.isNotEmpty()) {
                scanners.add(Scanner(name, BeaconCloud(Rotation(Vector3D(0.0, 0.0, 1.0), 0.0), currentPoints.toMutableList()), listOf()))
                currentPoints = mutableSetOf()
            }
            name = match.groupValues[1]
            continue
        }
        val vecMatch = vecRegex.matchEntire(line)
        if (vecMatch != null) {
            currentPoints.add(
                Vector3D(
                    vecMatch.groupValues[1].toInt().toDouble(),
                    vecMatch.groupValues[2].toInt().toDouble(),
                    vecMatch.groupValues[3].toInt().toDouble()
                )
            )
        }
    }
    if (currentPoints.isNotEmpty()) {
        scanners.add(Scanner(name,  BeaconCloud(Rotation(Vector3D(0.0, 0.0, 1.0), 0.0), currentPoints.toMutableList()), listOf()))
    }
    return scanners.toList()
}

fun generateClouds(vecs : List<Vector3D>) : List<BeaconCloud> {
    val clouds = mutableListOf(
        vecs.map { Vector3D(it.x, it.y, it.z) }.toList(),
        vecs.map { Vector3D(it.x, it.z, -it.y) }.toList(),
        vecs.map { Vector3D(it.x, -it.y, -it.z) }.toList(),
        vecs.map { Vector3D(it.x, -it.z, it.y) }.toList(),
        vecs.map { Vector3D(-it.z, it.y, it.x) }.toList(),
        vecs.map { Vector3D(it.z, it.y, -it.x) }.toList(),
    )

    val vx = Vector3D(1.0, 0.0, 0.0)
    val vy = Vector3D(0.0, 1.0, 0.0)
    val vz = Vector3D(0.0, 0.0, 1.0)
    var rotations = listOf<Rotation>(
        Rotation(vx, PI / 2.0), Rotation(vx, PI), Rotation(vx, PI * 3.0 / 2.0),
        Rotation(vy, PI / 2.0), Rotation(vy, PI), Rotation(vy, PI * 3.0 / 2.0),
        Rotation(vz, PI / 2.0), Rotation(vz, PI), Rotation(vz, PI * 3.0 / 2.0),
    )

    var beaconClouds = clouds.map { BeaconCloud(Rotation(Vector3D(0.0, 0.0, 1.0), 0.0), it.toMutableList()) }.toMutableList()
    for (c in clouds) {
        for (r in rotations) {
            beaconClouds.add(BeaconCloud(r, c.map { Vector3D(r.applyTo(it).toArray().map { it.roundToInt() }.map { it.toDouble() }.toDoubleArray() ) }.toMutableList()))
        }
    }
    return beaconClouds
}


fun intersectScanners(s1 : Scanner, s2 : Scanner) : Scanner? {
    val s2Beacons = s2.beaconClouds.plus(s2.original)
        for (bc2 in s2Beacons.indices) {
            val diffMap = s1.original.points
                .map { bc1p -> s2Beacons[bc2].points.map { bc2p -> bc1p.subtract(bc2p) } }.flatten()
                .map { it -> listOf(it.x.roundToInt(), it.y.roundToInt(), it.z.roundToInt()) }
                .groupBy { it }
                .mapValues { it.value.size }

            val intersect = diffMap.filterValues { it >= 12 }.keys
            if (intersect.isNotEmpty()) {
                val v = intersect.first()
                // the following code (translates new scanner to original scanner which in theory should result in some overlaps)
                val originalBeaconCloud = BeaconCloud(s2Beacons[bc2].rotation, s2Beacons[bc2].points)
                originalBeaconCloud.addVec(Vector3D(v.map { it.toDouble() }.toDoubleArray()))
                return Scanner(s2.name, originalBeaconCloud, listOf())
            }
        }

    return null
}

fun countBeacons(s : String) : Int {
    var scanners = parseScanners(s).map { scanner -> Scanner(scanner.name, scanner.original, generateClouds(scanner.original.points)) }
    var distances = mutableListOf<Vector3D>()
    var alignedScanners = mutableListOf(scanners[0])
    var unalignedScanners = scanners.drop(1).toMutableList()
    while (!unalignedScanners.isEmpty()) {
        var scanner = unalignedScanners.removeFirst()

        var firstIntersect : Scanner? = null
        for (alignedScanner in alignedScanners) {
            firstIntersect = intersectScanners(alignedScanner, scanner)
            if (firstIntersect != null) {
                break
            }
        }
        if (firstIntersect != null) {
            alignedScanners.add(firstIntersect)
        } else {
            unalignedScanners.add(scanner)
        }
    }
    return alignedScanners.map { it.original.points }.reduce { acc,v -> acc.union(v.toSet()).toMutableList() }.count()
}

fun main(args: Array<String>) {
    var fileName: String = args[0]
    try {
        val part1Result = countBeacons(loadFileAsString(fileName))
        println("Day 19 part 1 result: $part1Result")
        val part2Result = 0 // evaluateFileForMaxExpression(loadFileAsString(fileName))
        println("Day 19 part 2 result: $part2Result")
    } catch (e: FileNotFoundException) {
        println("Input file does not exist ($fileName)")
    }
}