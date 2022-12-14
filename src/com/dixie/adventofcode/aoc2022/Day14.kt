package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.lib.Space2D
import kotlin.math.max
import kotlin.math.min

private const val ROCK = '#'
private const val AIR = '.'
private const val SAND_SOURCE = '+'
private const val SAND_AT_REST = 'o'

class Day14 : Day() {
  private val cave = Space2D<Char>()
  private var floorY = 0
  private var unitsOfSand = 0

  override fun prepare() {
    cave.setValueAt(500, 0, SAND_SOURCE)
    lines.forEach {
      val coords = it.split(" -> ")
      var cur = coords[0].split(",").map(String::toInt)
      for (i in 1 until coords.size) {
        val next = coords[i].split(",").map(String::toInt)
        if (cur[0] == next[0]) {
          // Vertical line
          for (y in min(cur[1], next[1])..max(cur[1], next[1])) {
            cave.setValueAt(cur[0], y, ROCK)
          }
        } else {
          // Horizontal line
          for (x in min(cur[0], next[0])..max(cur[0], next[0])) {
            cave.setValueAt(x, cur[1], ROCK)
          }
        }
        cur = next
      }
    }
    // Construct the floor.
    floorY = cave.bounds.y + cave.bounds.height + 1
    for (x in -floorY..floorY) {
      cave.setValueAt(x + 500, floorY, ROCK)
    }
  }

  override fun solve(part1: Boolean): Any {
    var x = 500
    var y = 0
    while (cave.bounds.contains(x, y)) {
      if (part1 && y >= floorY - 2) break
      when (AIR) {
        cave.getValueAt(x, y + 1, AIR) -> {
          // Sand can fall directly down.
          y++
        }
        cave.getValueAt(x - 1, y + 1, AIR) -> {
          // Sand can flow down-and-left.
          x--
          y++
        }
        cave.getValueAt(x + 1, y + 1, AIR) -> {
          // Sand can flow down-and-right.
          x++
          y++
        }
        else -> {
          // Sand comes to rest.
          cave.setValueAt(x, y, SAND_AT_REST)
          unitsOfSand++
          if (y == 0) {
            return unitsOfSand
          }
          x = 500
          y = 0
        }
      }
    }
    return unitsOfSand
  }
}

fun main() {
  Day14().run()
}
