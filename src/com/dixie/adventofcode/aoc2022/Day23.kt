package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.lib.Direction
import com.dixie.adventofcode.lib.Space2D
import java.awt.Point

class Day23 : Day() {
  private lateinit var space: Space2D<Boolean>
  private val directions =
    listOf(
      listOf(Direction.NORTH, Direction.NORTH_WEST, Direction.NORTH_EAST),
      listOf(Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST),
      listOf(Direction.WEST, Direction.SOUTH_WEST, Direction.NORTH_WEST),
      listOf(Direction.EAST, Direction.SOUTH_EAST, Direction.NORTH_EAST),
    )

  override fun prepare() {
    space = Space2D.parseFromStrings(lines) { if (it == '#'.code) true else null }
  }

  override fun part1(): Any {
    (0 until 10).forEach(::simulateRound)
    val area = (space.bounds.width + 1) * (space.bounds.height + 1)
    val numElves = space.streamAllPoints().count()
    println(space.toPrintableImage { if (it != null) "#" else "." })
    return area - numElves
  }

  private fun simulateRound(round: Int) {
    val moves = mutableMapOf<Point, MutableList<Point>>()
    println(space.toPrintableImage { if (it != null) "#" else "." })
    // Propose moves.
    space.streamAllPoints().forEach { from ->
      if (!hasNeighbours(from)) return@forEach
      for (d in directions.indices) {
        val dirs = directions[(round + d) % directions.size]
        // Check space orthogonal.
        val next = dirs[0].apply(from)
        if (space.getValueAt(next, false)) continue
        // Check space diagonal.
        val side1 = dirs[1].apply(from)
        val side2 = dirs[2].apply(from)
        if (space.getValueAt(side1, false) || space.getValueAt(side2, false)) continue
        // Propose move.
        moves.computeIfAbsent(next) { mutableListOf() }.add(from)
        break
      }
    }

    // Make unique moves.
    moves.filterValues { it.size == 1 }.forEach { (to, from) ->
      space.removeValueAt(from[0])
      space.setValueAt(to, true)
    }
  }

  private fun hasNeighbours(point: Point): Boolean {
    for (dir in Direction.values()) {
      if (space.getValueAt(dir.apply(point), false)) return true
    }
    return false
  }
}

fun main() {
  Day23().run()
}
