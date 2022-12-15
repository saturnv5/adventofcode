package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*

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
    // Graph that can only move to adjacent squares that increase in elevation by at most 1.
    val graph =
      Graph.unweighted { cur: Pair<Int, Int> ->
        Direction.values()
          .asSequence()
          .map { cur.move(it) }
          .filter { next ->
            val height = if (cur == start) 'a' else heightMap[cur.first][cur.second]
            val (row, col) = next
            if (row !in heightMap.indices || col !in heightMap[0].indices) return@filter false
            val newHeight = if (heightMap[row][col] == 'E') 'z' else heightMap[row][col]
            newHeight.code - height.code <= 1
          }
      }
    return graph.findShortestPath(start, end)?.cost ?: "Path not found!"
  }

  override fun part2(): Any {
    // Move to adjacent squares in reverse direction that decrease in elevation by at most 1.
    val graph =
      Graph.unweighted { cur: Pair<Int, Int> ->
        Direction.values()
          .asSequence()
          .map { cur.move(it) }
          .filter { next ->
            val height = if (cur == end) 'z' else heightMap[cur.first][cur.second]
            val (row, col) = next
            if (row !in heightMap.indices || col !in heightMap[0].indices) return@filter false
            val newHeight = if (heightMap[row][col] == 'S') 'a' else heightMap[row][col]
            height.code - newHeight.code <= 1
          }
      }
    return graph
      .findShortestPath(
        end,
        { (row, col) -> heightMap[row][col] == 'a' || heightMap[row][col] == 'S' }
      )
      ?.cost
      ?: "Path not found!"
  }
}

fun main() {
  Day12().run()
}
