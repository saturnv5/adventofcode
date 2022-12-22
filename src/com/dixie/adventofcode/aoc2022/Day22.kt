package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.*

private const val WALL = '#'
private const val OPEN = '.'
private const val VOID = ' '
private const val FACE_SIZE = 50

class Day22 : Day() {
  private lateinit var map: List<List<Char>>
  private lateinit var start: Pair<Int, Int>
  private val instructions = mutableListOf<Instruction>()

  override fun prepare() {
    map = lines.takeWhile(String::isNotEmpty).toMatrix { it }
    start = map[0].indexOfFirst { it == OPEN } to 0

    val turns = lines.last().split("\\d+".toRegex()).filter(String::isNotEmpty)
    val stepsList = lines.last().split("[LR]".toRegex()).map(String::toInt)

    for (i in stepsList.indices) {
      val steps = stepsList[i]
      instructions +=
        if (i < turns.size)
          Instruction(steps, if (turns[i] == "L") Direction::turnLeft else Direction::turnRight)
        else Instruction(steps) { it }
    }
  }

  override fun solve(part1: Boolean): Any {
    var pos = start
    var dir = Direction.RIGHT
    instructions.forEach {
      val next = move(pos, dir, it.steps, part1)
      pos = next.first
      dir = it.turn(next.second)
    }
    val row = pos.y + 1
    val col = pos.x + 1
    val dirNum = Math.floorMod(dir.ordinal - Direction.RIGHT.ordinal, Direction.values().size)
    return 1000 * row + 4 * col + dirNum
  }

  private fun move(
    from: Pair<Int, Int>,
    fromDir: Direction,
    steps: Int,
    part1: Boolean,
  ): Pair<Pair<Int, Int>, Direction> {
    var pos = from
    var dir = fromDir
    repeat(steps) {
      val (nextPos, nextDir) = if (part1) moveOnMap(pos, dir) else moveOnCube(pos, dir)
      if (map[nextPos.y][nextPos.x] == WALL) return pos to dir
      pos = nextPos
      dir = nextDir
    }
    return pos to dir
  }

  private fun moveOnMap(from: Pair<Int, Int>, dir: Direction): Pair<Pair<Int, Int>, Direction> {
    val newPos = from.move(dir)

    if (
      newPos.y in map.indices &&
        newPos.x in map[newPos.y].indices &&
        map[newPos.y][newPos.x] != VOID
    ) {
      // No wrapping necessary.
      return newPos to dir
    }

    var (x, y) = newPos

    if (dir == Direction.RIGHT || dir == Direction.LEFT) {
      // Horizontal wrapping.
      if (y < FACE_SIZE) {
        if (x == FACE_SIZE - 1) x += FACE_SIZE * 2 else if (x == FACE_SIZE * 3) x -= FACE_SIZE * 2
      } else if (y < FACE_SIZE * 2) {
        if (x == FACE_SIZE - 1) x += FACE_SIZE else if (x == FACE_SIZE * 2) x -= FACE_SIZE
      } else if (y < FACE_SIZE * 3) {
        if (x == -1) x += FACE_SIZE * 2 else if (x == FACE_SIZE * 2) x = 0
      } else {
        if (x == -1) x += FACE_SIZE else if (x == FACE_SIZE) x = 0
      }
    } else {
      // Vertical wrapping.
      if (x < FACE_SIZE) {
        if (y == FACE_SIZE * 2 - 1) y += FACE_SIZE * 2
        else if (y == FACE_SIZE * 4) y -= FACE_SIZE * 2
      } else if (x < FACE_SIZE * 2) {
        if (y == -1) y += FACE_SIZE * 3 else if (y == FACE_SIZE * 3) y = 0
      } else {
        if (y == -1) y += FACE_SIZE else if (y == FACE_SIZE) y = 0
      }
    }

    return (x to y) to dir
  }

  private fun moveOnCube(from: Pair<Int, Int>, dir: Direction): Pair<Pair<Int, Int>, Direction> {
    val newPos = from.move(dir)

    if (
      newPos.y in map.indices &&
        newPos.x in map[newPos.y].indices &&
        map[newPos.y][newPos.x] != VOID
    ) {
      // No wrapping necessary.
      return newPos to dir
    }

    var (x, y) = newPos
    var newDir = dir

    /* Cube layout:
     *
     *    A B
     *    C
     *  E D
     *  F
     */

    if (dir == Direction.RIGHT || dir == Direction.LEFT) {
      // Horizontal wrapping.
      if (y < FACE_SIZE) {
        if (x == FACE_SIZE - 1) {
          // A -> E
          x = 0
        } else if (x == FACE_SIZE * 3) {
          // B -> D
          x = FACE_SIZE * 2 - 1
        }
        y = FACE_SIZE * 3 - y - 1
        newDir = dir.turnAround()
      } else if (y < FACE_SIZE * 2) {
        if (x == FACE_SIZE - 1) {
          // C -> E
          x = y - FACE_SIZE
          y = FACE_SIZE * 2
        } else if (x == FACE_SIZE * 2) {
          // C -> B
          x = y + FACE_SIZE
          y = FACE_SIZE - 1
        }
        newDir = dir.turnLeft()
      } else if (y < FACE_SIZE * 3) {
        if (x == -1) {
          // E -> A
          x = FACE_SIZE
        } else if (x == FACE_SIZE * 2) {
          // D -> B
          x = FACE_SIZE * 3 - 1
        }
        y = FACE_SIZE * 3 - y - 1
        newDir = dir.turnAround()
      } else {
        if (x == -1) {
          // F -> A
          x = y - FACE_SIZE * 2
          y = 0
        } else if (x == FACE_SIZE) {
          // F -> D
          x = y - FACE_SIZE * 2
          y = FACE_SIZE * 3 - 1
        }
        newDir = dir.turnLeft()
      }
    } else {
      // Vertical wrapping.
      if (x < FACE_SIZE) {
        if (y == FACE_SIZE * 2 - 1) {
          // E -> C
          y = FACE_SIZE + x
          x = FACE_SIZE
          newDir = dir.turnRight()
        } else if (y == FACE_SIZE * 4) {
          // F -> B
          x += FACE_SIZE * 2
          y = 0
        }
      } else if (x < FACE_SIZE * 2) {
        if (y == -1) {
          // A -> F
          y = FACE_SIZE * 2 + x
          x = 0
        } else if (y == FACE_SIZE * 3) {
          // D -> F
          y = FACE_SIZE * 2 + x
          x = FACE_SIZE - 1
        }
        newDir = dir.turnRight()
      } else {
        if (y == -1) {
          // B -> F
          x -= FACE_SIZE * 2
          y = FACE_SIZE * 4 - 1
        } else if (y == FACE_SIZE) {
          // B -> C
          y = x - FACE_SIZE
          x = FACE_SIZE * 2 - 1
          newDir = dir.turnRight()
        }
      }
    }

    return (x to y) to newDir
  }

  private class Instruction(val steps: Int, val turn: (Direction) -> Direction)
}

fun main() {
  Day22().run()
}
