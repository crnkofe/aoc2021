package main.day20

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day20Tests {
    @Test
    fun shouldParseBinary() {
        assertEquals(32, toInt("000100000"))
        assertEquals(511, toInt("111111111"))
    }

    @Test
    fun shouldParseSamples() {
        val input = """
            ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
            #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
            .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
            .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
            .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
            ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
            ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

            #..#.
            #....
            ##..#
            ..#..
            ..###
        """.trimIndent()
        val algo = parseAlgo(input)
        assertEquals(512, algo.length)

        var img = parseImage(input)
        draw(img)

        val img1 = enhance(img, algo, 1)
        draw(img1)

        val img2 = enhance(img1, algo, 1)
        draw(img2)

        assertEquals(35, img2.values.filter { it == '#' }.size)

        for (i in (0 until 50)) {
            img = enhance(img, algo, 1)
        }
        assertEquals(3351, img.values.filter { it == '#' }.size)
    }
}