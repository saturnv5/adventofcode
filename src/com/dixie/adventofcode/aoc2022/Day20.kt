package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day20 : Day() {
  private lateinit var numbers: List<Int>

  override fun prepare() {
    numbers = lines.map(String::toInt)
  }

  override fun part1(): Any {
    return calculateGroveCoordinate(1, 1)
  }

  override fun part2(): Any {
    return calculateGroveCoordinate(811_589_153L, 10)
  }

  private fun calculateGroveCoordinate(key: Long, numMixes: Int): Long {
    // Map to index to create unique entries for each element in the list.
    val initialList = numbers.map { it * key }.withIndex()
    val mixedList = initialList.toMutableList()

    repeat(numMixes) {
      initialList.forEach {
        // Remove element from the current index first.
        val currentIndex = mixedList.indexOf(it)
        mixedList.removeAt(currentIndex)

        // Then advance by the value, modulo the smaller list size (n - 1), and re-insert value.
        val finalIndex = Math.floorMod(currentIndex + it.value, mixedList.size)
        mixedList.add(finalIndex, it)
      }
    }

    val startIndex = mixedList.indexOfFirst { it.value == 0L }
    return mixedList[(startIndex + 1000) % mixedList.size].value +
      mixedList[(startIndex + 2000) % mixedList.size].value +
      mixedList[(startIndex + 3000) % mixedList.size].value
  }
}

fun main() {
  Day20().run()
}
