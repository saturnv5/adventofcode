package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day3 : Day() {
  override fun part1(): Any {
    return lines
      .map {
        val comp1 = it.subSequence(0, it.length / 2).toSet()
        val comp2 = it.subSequence(it.length / 2, it.length).toSet()
        val common = comp1 intersect comp2
        common.first()
      }
      .map(::priority)
      .sum()
  }

  override fun part2(): Any {
    var sum = 0
    for (i in lines.indices step 3) {
      var common = lines[i].toSet()
      common = common intersect lines[i + 1].toSet()
      common = common intersect lines[i + 2].toSet()
      sum += priority(common.first())
    }
    return sum
  }

  private fun priority(item: Char): Int {
    return if (item.isLowerCase()) item - 'a' + 1 else item - 'A' + 27
  }
}

fun main() {
  Day3().run()
}
