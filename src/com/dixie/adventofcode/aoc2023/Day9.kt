package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.toInts

class Day9 : Day() {
  override fun solve(part1: Boolean) =
    lines.sumOf { line -> extrapolate(line.toInts().toList(), part1) }

  private fun extrapolate(sequence: List<Int>, forwards: Boolean): Int {
    val deltas = mutableListOf<Int>()
    for (i in sequence.indices) {
      if (i == 0) continue
      deltas += sequence[i] - sequence[i - 1]
    }
    return if (forwards) {
      if (deltas.none { it != 0 }) sequence.last()
      else sequence.last() + extrapolate(deltas, true)
    } else {
      if (deltas.none { it != 0 }) sequence.first()
      else sequence.first() - extrapolate(deltas, false)
    }
  }
}

fun main() {
  Day9().run()
}
