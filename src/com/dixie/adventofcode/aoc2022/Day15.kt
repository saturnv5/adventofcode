package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*
import com.google.common.collect.Range
import kotlin.math.abs

class Day15 : Day() {
  private val beacons = mutableSetOf<Pair<Int, Int>>()
  private lateinit var sensors: List<Sensor>

  override fun prepare() {
    sensors =
      lines.map {
        val tokens =
          it.extractNumbers().split(" +".toRegex()).filter(String::isNotEmpty).map(String::toInt)
        val location = tokens[0] to tokens[1]
        val beacon = tokens[2] to tokens[3]
        beacons += beacon
        Sensor(location, beacon)
      }
    super.prepare()
  }

  override fun part1(): Any {
    return exclusionSizeAtRow(2_000_000)
  }

  private fun exclusionSizeAtRow(row: Int): Int {
    val beaconsOnRow = beacons.filter { it.y == row }.map { it.x }.toMutableSet()
    var size = 0

    computeExclusionRangesAtRow(row).forEach { range ->
      val beaconsInRange = beaconsOnRow.filter(range::contains)
      size += range.upperEndpoint() - range.lowerEndpoint() + 1
      if (beaconsInRange.isNotEmpty()) {
        beaconsOnRow.removeAll(beaconsInRange.toSet())
        size -= beaconsInRange.size
      }
    }

    return size
  }

  private fun computeExclusionRangesAtRow(row: Int): List<Range<Int>> {
    val ranges =
      sensors.mapNotNull { it.exclusionRangeAtRow(row) }.sortedBy(Range<Int>::lowerEndpoint)

    val mergedRanges = mutableListOf<Range<Int>>()
    var currentRange = ranges[0]

    for (i in 1 until ranges.size) {
      var nextRange = ranges[i]
      if (currentRange.isConnected(nextRange)) {
        currentRange = currentRange.span(nextRange)
      } else {
        mergedRanges += currentRange
        currentRange = nextRange
      }
    }
    mergedRanges += currentRange

    return mergedRanges
  }
}

private class Sensor(val location: Pair<Int, Int>, val closestBeacon: Pair<Int, Int>) {
  val beaconDistance = abs(location.x - closestBeacon.x) + abs(location.y - closestBeacon.y)

  fun exclusionRangeAtRow(row: Int): Range<Int>? {
    val radius = beaconDistance - abs(location.y - row)
    return if (radius >= 0) {
      Range.closed(location.x - radius, location.x + radius)
    } else {
      null
    }
  }
}

fun main() {
  Day15().run()
}
