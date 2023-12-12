package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.*

class Day10 : Day() {
  private lateinit var pipes: List<List<Pipe>>
  private lateinit var start: Pair<Int, Int>

  override fun prepare() {
    pipes =
      lines.toMatrix {
        when (it) {
          '.' -> Pipe.GROUND
          'S' -> Pipe.START
          '-' -> Pipe.HORIZONTAL
          '|' -> Pipe.VERTICAL
          'J' -> Pipe.UP_AND_LEFT
          'L' -> Pipe.UP_AND_RIGHT
          '7' -> Pipe.DOWN_AND_LEFT
          'F' -> Pipe.DOWN_AND_RIGHT
          else -> throw IllegalArgumentException()
        }
      }
    start = pipes.matrixPositions().find { (x, y) -> pipes[y][x] == Pipe.START }!!
  }

  override fun part1(): Any {
    var current = start
    var from = start
    var steps = 0
    do {
      var tmp = current
      current = current.next(from)
      from = tmp
      steps++
    } while (current != start)
    return steps / 2
  }

  private fun Pair<Int, Int>.next(from: Pair<Int, Int>): Pair<Int, Int> {
    val pipe = pipes[this.y][this.x]
    if (pipe == Pipe.START) {
      return Direction.values().map(this::move).find { (x, y) -> pipes[y][x] != Pipe.GROUND }!!
    }
    val first = this.move(pipe.connections!!.first)
    return if (first == from) this.move(pipe.connections!!.second) else first
  }

  private enum class Pipe(val connections: Pair<Direction, Direction>?) {
    GROUND(null),
    START(null),
    HORIZONTAL(Direction.LEFT to Direction.RIGHT),
    VERTICAL(Direction.UP to Direction.DOWN),
    UP_AND_LEFT(Direction.UP to Direction.LEFT),
    UP_AND_RIGHT(Direction.UP to Direction.RIGHT),
    DOWN_AND_LEFT(Direction.DOWN to Direction.LEFT),
    DOWN_AND_RIGHT(Direction.DOWN to Direction.RIGHT),
  }
}

fun main() {
  Day10().run()
}
