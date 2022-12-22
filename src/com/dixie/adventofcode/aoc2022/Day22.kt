package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*

private const val VOID = ' '
private const val WALL = '#'
private const val OPEN = '.'

class Day22 : Day() {
  private lateinit var map: List<List<Char>>
  private lateinit var start: Pair<Int, Int>
  private var width = 0
  private val instructions = mutableListOf<Instruction>()

  override fun prepare() {
    map = lines.takeWhile(String::isNotEmpty).toMatrix { it }
    start = map[0].indexOfFirst { it == OPEN } to 0
    width = map.maxOf(List<Char>::size)

    var dir = Direction.RIGHT
    val turns = lines.last().split("\\d+".toRegex()).filter(String::isNotEmpty)
    val stepsList = lines.last().split("L|R".toRegex()).map(String::toInt)

    for (i in stepsList.indices) {
      val steps = stepsList[i]
      instructions += Instruction(dir, steps)
      if (i < turns.size) {
        dir = if (turns[i] == "L") dir.turnLeft() else dir.turnRight()
      }
    }
  }

  override fun part1(): Any {
    var pos = start
    instructions.forEach { pos = nextPosition(pos, it) }
    val row = pos.y + 1
    val col = pos.x + 1
    val dir =
      Math.floorMod(
        instructions.last().dir.ordinal - Direction.RIGHT.ordinal,
        Direction.values().size
      )
    return 1000 * row + 4 * col + dir
  }

  private fun tile(pos: Pair<Int, Int>) = if (map[pos.y].size > pos.x) map[pos.y][pos.x] else VOID

  private fun nextPosition(from: Pair<Int, Int>, inst: Instruction): Pair<Int, Int> {
    var pos = from
    repeat(inst.steps) {
      var nextPos = pos
      do {
        nextPos = nextPosition(nextPos, inst.dir)
      } while (tile(nextPos) == VOID)
      if (tile(nextPos) == WALL) return pos
      pos = nextPos
    }
    return pos
  }

  private fun nextPosition(from: Pair<Int, Int>, dir: Direction): Pair<Int, Int> {
    var newPos = from.move(dir)
    return if (newPos.x in (0 until width) && newPos.y in map.indices) newPos
    else Math.floorMod(newPos.x, width) to Math.floorMod(newPos.y, map.size)
  }

  private class Instruction(val dir: Direction, val steps: Int)
}

fun main() {
  Day22().run()
}
