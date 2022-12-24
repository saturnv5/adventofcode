package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*

private const val WALL = '#'
private const val EMPTY = '.'

class Day24 : Day() {
  private val directions =
    mutableMapOf(
      '<' to Direction.LEFT,
      '^' to Direction.UP,
      '>' to Direction.RIGHT,
      'v' to Direction.DOWN
    )
  private lateinit var valley: Valley
  private lateinit var graph: Graph<State>

  override fun prepare() {
    val map = lines.toMatrix { it }
    val width = map[0].size - 2
    val height = map.size - 2
    val initialBlizzards = mutableMapOf<Pair<Int, Int>, Direction>()
    map
      .matrixPositions()
      .filter { (x, y) -> map[y][x] in directions.keys }
      .forEach { (x, y) ->
        val dir = directions[map[y][x]]!!
        initialBlizzards[x - 1 to y - 1] = dir
      }
    valley = Valley(width, height, initialBlizzards)

    graph =
      Graph.unweighted { (pos, time): State ->
        val newTime = time + 1
        // New positions includes not moving.
        val newPositions =
          (0..4).asSequence().map { if (it < 4) pos.move(Direction.values()[it]) else pos }
        newPositions.filter { valley.safeToMove(it, newTime) }.map { State(it, newTime) }
      }
  }

  override fun part1(): Any {
    val start = State(valley.startPoint, 0)
    return graph.findShortestPath(start, { it.pos == valley.endPoint })!!.cost
  }

  override fun part2(): Any {
    val time1 =
      graph
        .findShortestPath(State(valley.startPoint, 0), { it.pos == valley.endPoint })!!
        .cost
        .toInt()
    val time2 =
      graph
        .findShortestPath(State(valley.endPoint, time1), { it.pos == valley.startPoint })!!
        .cost
        .toInt()
    val time3 =
      graph
        .findShortestPath(State(valley.startPoint, time1 + time2), { it.pos == valley.endPoint })!!
        .cost
        .toInt()
    return time1 + time2 + time3
  }

  private data class State(val pos: Pair<Int, Int>, val time: Int)

  private class Valley(
    val width: Int,
    val height: Int,
    initialBlizzards: Map<Pair<Int, Int>, Direction>
  ) {
    val startPoint = 0 to -1
    val endPoint = width - 1 to height

    private val blizzardTimeline: MutableList<Map<Pair<Int, Int>, List<Direction>>>
    init {
      val blizzards = mutableMapOf<Pair<Int, Int>, List<Direction>>()
      initialBlizzards.forEach { (pos, dirs) -> blizzards[pos] = listOf(dirs) }
      blizzardTimeline = mutableListOf(blizzards)
    }

    fun safeToMove(pos: Pair<Int, Int>, time: Int): Boolean {
      if (pos == startPoint || pos == endPoint) return true
      if (pos.x !in 0 until width || pos.y !in 0 until height) return false
      generateUpToTime(time)
      return !blizzardTimeline[time].containsKey(pos)
    }

    private fun generateUpToTime(time: Int) {
      while (blizzardTimeline.size <= time) {
        val newBlizzards = mutableMapOf<Pair<Int, Int>, MutableList<Direction>>()
        blizzardTimeline.last().forEach { (pos, dirs) ->
          for (dir in dirs) {
            var newPos = pos.move(dir)
            newPos = wrapPosition(newPos)
            newBlizzards.computeIfAbsent(newPos) { mutableListOf() }.add(dir)
          }
        }

        blizzardTimeline += newBlizzards
      }
    }

    private fun wrapPosition(pos: Pair<Int, Int>): Pair<Int, Int> {
      if (pos.x in 0 until width && pos.y in 0 until height) return pos
      var (x, y) = pos
      x = Math.floorMod(x, width)
      y = Math.floorMod(y, height)
      return x to y
    }
  }
}

fun main() {
  Day24().run()
}
