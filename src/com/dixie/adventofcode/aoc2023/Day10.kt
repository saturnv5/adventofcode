package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.*

class Day10 : Day() {
  private lateinit var pipes: List<List<Pipe>>
  private lateinit var loop: List<Pair<Int, Int>>
  private lateinit var loopSet: Set<Pair<Int, Int>>

  override fun prepare() {
    pipes = lines.toMatrix { ch -> Pipe.values().find { it.symbol == ch }!! }
    val start = pipes.matrixPositions().find { (x, y) -> pipes[y][x] == Pipe.START }!!
    pipes =
      pipes.toMutableList().mapIndexed { y, row ->
        if (y != start.y) row
        else {
          val newRow = row.toMutableList()
          newRow[start.x] = disambiguateStart(start)
          newRow
        }
      }
    val loop = mutableListOf<Pair<Int, Int>>()
    // Follow the loop from 'S'
    var current = start
    var from = start
    var steps = 0
    do {
      loop += current
      var tmp = current
      current = current.next(from)
      from = tmp
      steps++
    } while (current != start)
    this.loop = loop
    this.loopSet = loop.toSet()
  }

  override fun part1() = loop.size / 2

  override fun part2(): Any {
    val enclosed =
      pipes.matrixPositions().filterNot(loopSet::contains).filter(::isWithinLoop).toList()
    return enclosed.size
  }

  private fun isWithinLoop(pos: Pair<Int, Int>): Boolean {
    // Ray-march to the right and see how many times it intersects the loop.
    val overlaps =
      ((pos.x + 1) until pipes[pos.y].size)
        .asSequence()
        .map { x -> x to pos.y }
        .filter(loopSet::contains)
        .map { (x, y) -> pipes[y][x] }
        .toList()
    var intersections = 0
    var prev = Pipe.GROUND
    for (pipe in overlaps) {
      if (pipe == Pipe.HORIZONTAL) continue
      if (prev.isConnectedRight(pipe) && prev.isInverseOf(pipe)) {
        prev = Pipe.GROUND
      } else {
        prev = pipe
        intersections++
      }
    }
    return intersections % 2 != 0
  }

  private fun disambiguateStart(start: Pair<Int, Int>): Pipe {
    val dirs =
      Direction.values()
        .filter { dir ->
          val (x, y) = start.move(dir)
          val neighbour = pipes[y][x]
          if (neighbour.connections == null) false
          val opDir = dir.turnAround()
          opDir == neighbour.connections!!.first || opDir == neighbour.connections!!.second
        }
        .toList()
    return Pipe.values()
      .filter { it.connections != null }
      .find { p: Pipe ->
        dirs.contains(p.connections!!.first) && dirs.contains(p.connections!!.second)
      }!!
  }

  private fun Pair<Int, Int>.next(from: Pair<Int, Int>): Pair<Int, Int> {
    val pipe = pipes[this.y][this.x]
    if (pipe == Pipe.START) {
      return Direction.values().map(this::move).find { (x, y) -> pipes[y][x] != Pipe.GROUND }!!
    }
    val first = this.move(pipe.connections!!.first)
    return if (first == from) this.move(pipe.connections!!.second) else first
  }

  private fun Pipe.isConnectedRight(other: Pipe): Boolean {
    return (this == Pipe.HORIZONTAL || this == Pipe.DOWN_AND_RIGHT || this == Pipe.UP_AND_RIGHT) &&
      (other == Pipe.HORIZONTAL || other == Pipe.DOWN_AND_LEFT || other == Pipe.UP_AND_LEFT)
  }

  private fun Pipe.isInverseOf(other: Pipe): Boolean {
    if (this == Pipe.GROUND || other == Pipe.GROUND) return false
    return setOf(this.connections!!.first, this.connections!!.second)
      .intersect(setOf(other.connections!!.first, other.connections!!.second))
      .isEmpty()
  }

  private enum class Pipe(val symbol: Char, val connections: Pair<Direction, Direction>?) {
    GROUND('.', null),
    START('S', null),
    HORIZONTAL('-', Direction.LEFT to Direction.RIGHT),
    VERTICAL('|', Direction.UP to Direction.DOWN),
    UP_AND_LEFT('J', Direction.UP to Direction.LEFT),
    UP_AND_RIGHT('L', Direction.UP to Direction.RIGHT),
    DOWN_AND_LEFT('7', Direction.DOWN to Direction.LEFT),
    DOWN_AND_RIGHT('F', Direction.DOWN to Direction.RIGHT),
  }
}

fun main() {
  Day10().run()
}
