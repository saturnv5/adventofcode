package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.Graph
import com.dixie.adventofcode.klib.toInts
import kotlin.math.max
import kotlin.math.min

class Day18 : Day() {
  private lateinit var cubes: Set<Cube>
  private var minimum = Cube(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
  private var maximum = Cube(0, 0, 0)

  override fun prepare() {
    cubes =
      lines
        .map {
          val tokens = it.toInts(",").toList()
          val cube = Cube(tokens[0], tokens[1], tokens[2])
          minimum = Cube(min(minimum.x, cube.x), min(minimum.y, cube.y), min(minimum.z, cube.z))
          maximum = Cube(max(maximum.x, cube.x), max(maximum.y, cube.y), max(maximum.z, cube.z))

          return@map cube
        }
        .toSet()
  }

  override fun part1(): Any {
    return numHiddenFaces(cubes)
  }

  override fun part2(): Any {
    val graph =
      Graph.unweighted { cube: Cube ->
        Direction3D.values().asSequence().map(cube::move).filter {
          return@filter if (cubes.contains(it)) false
          else if (it.x < minimum.x - 1 || it.x > maximum.x + 1) false
          else if (it.y < minimum.y - 1 || it.y > maximum.y + 1) false
          else if (it.z < minimum.z - 1 || it.z > maximum.z + 1) false else true
        }
      }

    val externalAir = mutableSetOf<Cube>()
    graph.breadthFirstTraversal(minimum.copy(x = minimum.x - 1), { externalAir += it })

    val solidLava = mutableSetOf<Cube>()
    for (x in minimum.x..maximum.x) {
      for (y in minimum.y..maximum.y) {
        for (z in minimum.z..maximum.z) {
          val cube = Cube(x, y, z)
          if (!externalAir.contains(cube)) {
            solidLava += cube
          }
        }
      }
    }

    return numHiddenFaces(solidLava)
  }
}

private fun numHiddenFaces(cubes: Set<Cube>): Int {
  var hiddenFaces = 0
  for (cube in cubes) {
    hiddenFaces += numHiddenFaces(cube, cubes)
  }
  return cubes.size * 6 - hiddenFaces
}

private fun numHiddenFaces(cube: Cube, cubes: Set<Cube>): Int {
  return Direction3D.values().count { cubes.contains(cube.move(it)) }
}

private data class Cube(val x: Int, val y: Int, val z: Int) {
  fun move(dir: Direction3D) = Cube(x + dir.dx, y + dir.dy, z + dir.dz)
}

enum class Direction3D(val dx: Int, val dy: Int, val dz: Int) {
  UP(0, -1, 0),
  DOWN(0, 1, 0),
  LEFT(-1, 0, 0),
  RIGHT(1, 0, 0),
  FORWARD(0, 0, 1),
  BACKWARD(0, 0, -1),
}

fun main() {
  Day18().run()
}
