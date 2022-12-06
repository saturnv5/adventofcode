package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day6 : Day() {
  override fun part1(): Any {
    val msg = lines[0]
    for (i in 4..msg.length) {
      val set = setOf(msg[i - 4], msg[i - 3], msg[i - 2], msg[i - 1])
      if (set.size == 4) return i
    }
    return "Start-of-packet marker not found!"
  }

  override fun part2(): Any {
    val msg = lines[0]
    for (i in 14..msg.length) {
      val set = (1..14).map { msg[i - it] }.toSet()
      if (set.size == 14) return i
    }
    return "Message marker not found!"
  }
}

fun main() {
  Day6().run()
}
