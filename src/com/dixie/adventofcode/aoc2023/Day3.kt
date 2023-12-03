package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.lib.Direction
import java.awt.Point

class Day3 : Day() {
  private lateinit var numbers: Map<Point, Number>
  private lateinit var specials: List<Point>

  override fun prepare() {
    val numbers = mutableMapOf<Point, Number>()
    val specials = mutableListOf<Point>()
    for (y in lines.indices) {
      val line = lines[y]
      val digits = mutableListOf<Char>()
      val locations = mutableListOf<Point>()
      for (x in line.indices) {
        val ch = line[x]
        val loc = Point(x, y)
        if (ch.isDigit()) {
          digits += line[x]
          locations += loc
        }
        if (x == line.lastIndex || !ch.isDigit()) {
          if (!ch.isDigit() && ch != '.') specials += loc
          if (locations.isNotEmpty()) {
            val num = Number(locations[0], digits.joinToString("").toInt())
            locations.forEach { numbers[it] = num }
            digits.clear()
            locations.clear()
          }
        }
      }
    }
    this.numbers = numbers
    this.specials = specials
  }

  override fun part1(): Any {
    val partNumbers = mutableSetOf<Number>()
    specials.forEach { p ->
      for (neighbour in neighbours(p)) {
        numbers[neighbour]?.let(partNumbers::add)
      }
    }
    return partNumbers.sumOf(Number::value)
  }

  override fun part2() =
    specials
      .asSequence()
      .filter { p -> lines[p.y][p.x] == '*' }
      .map { neighbours(it).mapNotNull(numbers::get).toSet().toList() }
      .filter { it.size == 2 }
      .sumOf { it[0].value * it[1].value }

  private fun neighbours(point: Point) = Direction.values().map { it.apply(point) }

  data class Number(val start: Point, val value: Int)
}

fun main() {
  Day3().run()
}
