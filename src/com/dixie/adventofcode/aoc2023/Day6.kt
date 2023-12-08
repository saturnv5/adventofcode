package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers
import com.dixie.adventofcode.klib.toInts
import com.dixie.adventofcode.klib.toLongs

class Day6 : Day() {

  override fun part1(): Any {
    val times = lines[0].extractNumbers().toInts()
    val distances = lines[1].extractNumbers().toLongs().toList()
    return times
      .mapIndexed { i, time -> waysToBeat(time, distances[i])
      }
      .reduce { acc, i -> acc * i }
  }

  override fun part2(): Any {
    val time = lines[0].extractNumbers().filterNot { it == ' ' }.toInt()
    val distanceToBeat = lines[1].extractNumbers().filterNot { it == ' ' }.toLong()
    return waysToBeat(time, distanceToBeat)
  }

  private fun waysToBeat(time: Int, distanceToBeat: Long): Int {
    return (1 until time)
      .asSequence()
      .dropWhile { t -> t.toLong() * (time - t) <= distanceToBeat }
      .takeWhile { t -> t.toLong() * (time - t) > distanceToBeat }
      .count()
  }
}

fun main() {
  Day6().run()
}
