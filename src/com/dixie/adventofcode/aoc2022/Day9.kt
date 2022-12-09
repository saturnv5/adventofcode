package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import kotlin.math.abs

class Day9 : Day() {
  private val dirMap =
    mapOf('U' to Direction.UP, 'D' to Direction.DOWN, 'L' to Direction.LEFT, 'R' to Direction.RIGHT)

  override fun part1(): Any {
    var head = 0 to 0
    var tail = 0 to 0

    val visited = mutableSetOf(tail)

    lines.forEach {
      val dir = dirMap[it[0]]!!
      val dist = it.substring(2).toInt()
      repeat(dist) {
        head = head.move(dir)
        if (!isTailAdj(head, tail)) {
          tail = moveTail(head, tail)
          visited += tail
        }
      }
    }

    return visited.size
  }

  override fun part2(): Any {
    val knots = (1..10).map { 0 to 0 }.toMutableList()

    val visited = mutableSetOf(knots.last())

    lines.forEach {
      val dir = dirMap[it[0]]!!
      val dist = it.substring(2).toInt()
      repeat(dist) {
        knots[0] = knots[0].move(dir)
        for (i in 1 until knots.size) {
          val head = knots[i - 1]
          var tail = knots[i]
          if (!isTailAdj(head, tail)) {
            tail = moveTail(head, tail)
            knots[i] = tail
          }
        }
        visited += knots.last()
      }
    }

    return visited.size
  }

  private fun isTailAdj(head: Pair<Int, Int>, tail: Pair<Int, Int>): Boolean {
    return abs(head.first - tail.first) <= 1 && abs(head.second - tail.second) <= 1
  }

  private fun moveTail(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
    var x = tail.first
    var y = tail.second
    if (head.first < tail.first) x--
    else if (head.first > tail.first) x++
    if (head.second < tail.second) y--
    else if (head.second > tail.second) y++
    return x to y
  }
}

enum class Direction(val dx: Int, val dy: Int) {
  UP(0, -1),
  DOWN(0, 1),
  LEFT(-1, 0),
  RIGHT(1, 0)
}

private fun Pair<Int, Int>.move(dir: Direction) = (first + dir.dx) to (second + dir.dy)

fun main() {
  Day9().run()
}
