package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.then
import com.dixie.adventofcode.klib.toLongs

class Day5 : Day() {
  private lateinit var seedsToPlant: List<Long>
  private lateinit var seedToLocation: (Long) -> Long

  private var lineIndex = 0

  override fun prepare() {
    seedsToPlant = lines[lineIndex].substring(6).toLongs().toList()
    lineIndex = 3
    seedToLocation =
      parseNextMap() then
        parseNextMap() then
        parseNextMap() then
        parseNextMap() then
        parseNextMap() then
        parseNextMap() then
        parseNextMap()
  }

  private fun parseNextMap(): (Long) -> Long {
    val ranges = mutableListOf<Pair<LongRange, LongRange>>()
    while (lineIndex < lines.size && lines[lineIndex].isNotEmpty()) {
      val (dstStart, srcStart, length) = lines[lineIndex++].toLongs(" ").toList()
      ranges += (srcStart until srcStart + length) to (dstStart until dstStart + length)
    }
    ranges.sortBy { it.first.first }
    lineIndex += 2
    return { input ->
      var index = ranges.binarySearchBy(input, selector = { it.first.first })
      if (index < 0) {
        index = -index - 2
      }
      if (index !in ranges.indices || input !in ranges[index].first) input
      else input - ranges[index].first.first + ranges[index].second.first
    }
  }

  override fun part1() = seedsToPlant.minOf(seedToLocation)

  override fun part2(): Any {
    var i = 0
    var minimum = Long.MAX_VALUE
    while (i < seedsToPlant.size) {
      val seeds = seedsToPlant[i] until seedsToPlant[i] + seedsToPlant[i + 1]
      for (seed in seeds) {
        val loc = seedToLocation(seed)
        if (loc < minimum) minimum = loc
      }
      i += 2
    }
    return minimum
  }
}

fun main() {
  Day5().run()
}
