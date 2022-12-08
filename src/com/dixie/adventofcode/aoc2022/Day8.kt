package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.matrixIndices
import com.dixie.adventofcode.klib.toMatrix

class Day8 : Day() {
  private lateinit var grid: List<List<Int>>

  override fun prepare() {
    grid = lines.toMatrix(Char::digitToInt)
  }

  override fun part1(): Any {
    val visibleTrees = mutableSetOf<Pair<Int, Int>>()
    var height = 0
    // from left
    for (i in grid.indices) {
      height = -1
      for (j in grid[i].indices) {
        if (grid[i][j] > height) {
          visibleTrees.add(i to j)
          height = grid[i][j]
        }
      }
    }
    // from right
    for (i in grid.indices) {
      height = -1
      for (j in grid[i].size - 1 downTo 0) {
        if (grid[i][j] > height) {
          visibleTrees.add(i to j)
          height = grid[i][j]
        }
      }
    }
    // from top
    for (j in grid[0].indices) {
      height = -1
      for (i in grid.indices) {
        if (grid[i][j] > height) {
          visibleTrees.add(i to j)
          height = grid[i][j]
        }
      }
    }
    // from bottom
    for (j in grid[0].indices) {
      height = -1
      for (i in grid.size - 1 downTo 0) {
        if (grid[i][j] > height) {
          visibleTrees.add(i to j)
          height = grid[i][j]
        }
      }
    }
    return visibleTrees.size
  }

  override fun part2(): Any {
    return grid.matrixIndices().map { (i, j) -> scenicScore(i, j) }.max()
  }

  private fun scenicScore(row: Int, col: Int): Int {
    val height = grid[row][col]
    var score = 1
    // towards left
    var dist = 0
    for (j in col - 1 downTo 0) {
      dist++
      if (grid[row][j] >= height) break
    }
    score *= dist
    // towards right
    dist = 0
    for (j in col + 1 until grid[row].size) {
      dist++
      if (grid[row][j] >= height) break
    }
    score *= dist
    // towards up
    dist = 0
    for (i in row - 1 downTo 0) {
      dist++
      if (grid[i][col] >= height) break
    }
    score *= dist
    // towards down
    dist = 0
    for (i in row + 1 until grid.size) {
      dist++
      if (grid[i][col] >= height) break
    }
    score *= dist
    return score
  }
}

fun main() {
  Day8().run()
}
