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
      val minLoc =
        seeds
          .chuckRange(10_000_000L)
          .parallelStream()
          .map { it.minOf(seedToLocation) }
          .mapToLong { it }
          .min()
          .asLong
      if (minLoc < minimum) minimum = minLoc
      i += 2
    }
    return minimum
  }

  private fun LongRange.chuckRange(maxSize: Long): List<LongRange> {
    val ranges = mutableListOf<LongRange>()
    var i = this.first
    while (i <= this.last) {
      val j = kotlin.math.min(this.last + 1, i + maxSize)
      ranges += i until j
      i = j
    }
    return ranges
  }
}

fun main() {
  Day5().run()
}
