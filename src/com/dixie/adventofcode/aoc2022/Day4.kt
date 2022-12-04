package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day4 : Day() {
  private var pairs: List<Pair<IntRange, IntRange>> = listOf()

  override fun prepare() {
    pairs = lines.map {
      val ints = it.splitToSequence('-', ',').map(String::toInt).toList()
      val range1 = ints[0]..ints[1]
      val range2 = ints[2]..ints[3]
      range1 to range2
    }
  }

  override fun part1(): Any {
    return pairs.count {
      val intersection = it.first intersect it.second
      intersection.size == it.first.count() || intersection.size == it.second.count()
    }
  }

  override fun part2(): Any {
    return pairs.count {
      val intersection = it.first intersect it.second
      intersection.isNotEmpty()
    }
  }
}

fun main() {
  Day4().run()
}
