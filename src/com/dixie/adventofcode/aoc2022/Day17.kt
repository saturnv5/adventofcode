package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*
import com.dixie.adventofcode.lib.Space2D

class Day17 : Day() {
  private val rocks =
    listOf(
      Rock(4, listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)),
      Rock(3, listOf(1 to -2, 0 to -1, 1 to -1, 2 to -1, 1 to 0)),
      Rock(3, listOf(2 to -2, 2 to -1, 0 to 0, 1 to 0, 2 to 0)),
      Rock(1, listOf(0 to -3, 0 to -2, 0 to -1, 0 to 0)),
      Rock(2, listOf(0 to -1, 1 to -1, 0 to 0, 1 to 0))
    )
  private lateinit var jets: List<Direction>

  override fun prepare() {
    jets = lines[0].map { if (it == '<') Direction.LEFT else Direction.RIGHT }
  }

  override fun part1(): Any {
    val cave = Space2D<Boolean>()
    var jetIndex = 0
    for (i in 0 until 2022) {
      jetIndex = simulateTetrisRound(cave, i, jetIndex)
    }
    return -cave.bounds.y
  }

  override fun part2(): Any {
    val heights = mutableListOf<Int>()
    val repeatedCombos = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

    val cave = Space2D<Boolean>()
    var rockIndex = 0
    var jetIndex = 0
    var comboOccurrences: List<Int>
    while (true) {
      heights += -cave.bounds.y
      val occurrences =
        repeatedCombos.computeIfAbsent(rockIndex % rocks.size to jetIndex % jets.size) {
          mutableListOf()
        }
      occurrences += rockIndex
      if (occurrences.size >= 3) {
        comboOccurrences = occurrences
        break
      }

      jetIndex = simulateTetrisRound(cave, rockIndex++, jetIndex)
    }

    val totalReps = 1_000_000_000_000L
    var remaining = totalReps - comboOccurrences[1]
    val cycleLength = comboOccurrences[2] - comboOccurrences[1]
    val cycleReps = remaining / cycleLength
    remaining -= cycleLength * cycleReps

    val cycleStartHeight = heights[comboOccurrences[1]]
    val cycleHeight = heights[comboOccurrences[2]] - cycleStartHeight
    val remainderHeight = heights[comboOccurrences[1] + remaining.toInt()] - cycleStartHeight

    return cycleStartHeight + cycleHeight * cycleReps + remainderHeight
  }

  private fun simulateTetrisRound(cave: Space2D<Boolean>, rockIndex: Int, jetIndex: Int): Int {
    var j = jetIndex
    val rock = rocks[rockIndex % rocks.size]
    var pos = 2 to (cave.bounds.y - 4)
    while (true) {
      val jet = jets[j++ % jets.size]
      var newPos = pos.move(jet)
      if (cave.hasRoom(rock, newPos)) pos = newPos
      newPos = pos.move(Direction.DOWN)
      if (!cave.hasRoom(rock, newPos)) {
        cave.placeRock(rock, pos)
        // println(cave.toPrintableImage { if (it == null || !it) "." else "#" })
        break
      }
      pos = newPos
    }
    return j
  }

  private fun Space2D<Boolean>.hasRoom(rock: Rock, pos: Pair<Int, Int>): Boolean {
    if (pos.x < 0 || pos.x + rock.width > 7) return false
    if (pos.y >= 0) return false
    return rock.points.none { getValueAt(pos.x + it.x, pos.y + it.y, false) }
  }

  private fun Space2D<Boolean>.placeRock(rock: Rock, pos: Pair<Int, Int>) {
    rock.points.forEach { setValueAt(pos.x + it.x, pos.y + it.y, true) }
  }
}

private class Rock(val width: Int, val points: List<Pair<Int, Int>>)

fun main() {
  Day17().run()
}
