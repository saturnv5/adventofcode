package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import kotlin.math.max

class Day1 : Day() {
  override fun part1(): Any {
    var cals = 0
    var maxCals = 0
    for (line in lines) {
      if (line.isEmpty()) {
        maxCals = max(cals, maxCals)
        cals = 0
      } else {
        cals += line.toInt()
      }
    }
    return maxCals
  }

  override fun part2(): Any {
    var orderedCals = mutableListOf<Int>()
    var cals = 0
    for (line in lines) {
      if (line.isEmpty()) {
        orderedCals += cals
        cals = 0
      } else {
        cals += line.toInt()
      }
    }
    orderedCals.sortDescending()
    return orderedCals[0] + orderedCals[1] + orderedCals[2]
  }
}

fun main() {
  Day1().run()
}
