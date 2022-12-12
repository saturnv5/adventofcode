package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*
import java.util.PriorityQueue

class Day12 : Day() {
  private lateinit var heightMap: List<List<Char>>
  private lateinit var start: Pair<Int, Int>
  private lateinit var end: Pair<Int, Int>

  override fun prepare() {
    heightMap = lines.toMatrix { it }
    heightMap.matrixIndices().forEach { (row, col) ->
      if (heightMap[row][col] == 'S') {
        start = row to col
      } else if (heightMap[row][col] == 'E') {
        end = row to col
      }
    }
  }

  override fun part1(): Any {
    // Dijkstra shorted-path search.
    val fringe = PriorityQueue(Comparator.comparing(SearchNode::cost))
    fringe.add(SearchNode(start, 0))
    val visited = mutableSetOf<Pair<Int, Int>>()
    while (fringe.isNotEmpty()) {
      val cur = fringe.poll()
      if (!visited.add(cur.location)) continue
      val (row, col) = cur.location
      if (heightMap[row][col] == 'E') return cur.cost

      // Add neighbours that only climb at most one step.
      val height = if (cur.location == start) 'a' else heightMap[row][col]
      Direction.values()
        .map { cur.location.move(it) }
        .filter { next ->
          val (row, col) = next
          if (row !in heightMap.indices || col !in heightMap[0].indices) return@filter false
          if (visited.contains(next)) return@filter false
          val newHeight = if (heightMap[row][col]== 'E') 'z' else heightMap[row][col]
          newHeight.code - height.code <= 1
        }
        .map { SearchNode(it, cur.cost + 1) }
        .forEach(fringe::offer)
    }
    return "No path found!"
  }

  override fun part2(): Any {
    // Dijkstra shorted-path search, but in reverse.
    val fringe = PriorityQueue(Comparator.comparing(SearchNode::cost))
    fringe.add(SearchNode(end, 0))
    val visited = mutableSetOf<Pair<Int, Int>>()
    while (fringe.isNotEmpty()) {
      val cur = fringe.poll()
      if (!visited.add(cur.location)) continue
      val (row, col) = cur.location
      if (heightMap[row][col] == 'S' || heightMap[row][col] == 'a') return cur.cost

      // Add neighbours that only climb at most one step.
      val height = if (cur.location == end) 'z' else heightMap[row][col]
      Direction.values()
        .map { cur.location.move(it) }
        .filter { next ->
          val (row, col) = next
          if (row !in heightMap.indices || col !in heightMap[0].indices) return@filter false
          if (visited.contains(next)) return@filter false
          val newHeight = if (heightMap[row][col]== 'S') 'a' else heightMap[row][col]
          height.code - newHeight.code <= 1
        }
        .map { SearchNode(it, cur.cost + 1) }
        .forEach(fringe::offer)
    }
    return "No path found!"
  }
}

private data class SearchNode(val location: Pair<Int, Int>, val cost: Int)

fun main() {
  Day12().run()
}
