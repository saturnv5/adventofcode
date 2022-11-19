package com.dixie.adventofcode.aoc2021

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.toInts

class Day1 : Day() {
  override fun part1(): String {
    var incs = 0
    var prev = -1
    lines.toInts().forEach {
      if (prev >= 0 && it > prev) incs++
      prev = it
    }
    return "$incs"
  }

  override fun part2(): String {
    val depths = lines.toInts().toList()
    var incs = 0
    var prevSum = -1
    for (i in 0..depths.size - 3) {
      val sum = depths[i] + depths[i + 1] + depths[i + 2]
      if (prevSum >= 0 && sum > prevSum) incs++
      prevSum = sum
    }
    return "$incs"
  }
}

fun main() {
  Day1().run()
}
