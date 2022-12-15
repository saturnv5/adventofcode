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
    val targetRow = 2_000_000
    val beaconsOnRow = beacons.filter { it.y == targetRow }.map { it.x }.toMutableSet()
    var size = 0

    computeExclusionRangesAtRow(targetRow).forEach { range ->
      val beaconsInRange = beaconsOnRow.filter(range::contains)
      size += range.upperEndpoint() - range.lowerEndpoint() + 1
      if (beaconsInRange.isNotEmpty()) {
        beaconsOnRow.removeAll(beaconsInRange.toSet())
        size -= beaconsInRange.size
      }
    }

    return size
  }

  override fun part2(): Any {
    val maxCoord = 4_000_000
    val xRange = Range.closed(0, maxCoord)
    for (row in 0..maxCoord) {
      val exclusionRanges = computeExclusionRangesAtRow(row).filter(xRange::isConnected)
      if (exclusionRanges.size == 2) {
        val col = exclusionRanges[0].upperEndpoint() + 1
        return col.toLong() * maxCoord + row
      }
    }
    return "Distress beacon not found!"
  }

  private fun computeExclusionRangesAtRow(row: Int): List<Range<Int>> {
    val ranges =
      sensors.mapNotNull { it.exclusionRangeAtRow(row) }.sortedBy(Range<Int>::lowerEndpoint)

    if (ranges.isEmpty()) return ranges

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
