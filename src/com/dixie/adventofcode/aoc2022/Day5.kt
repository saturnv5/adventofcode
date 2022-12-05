package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day5 : Day() {
  private var stacks = mutableListOf<ArrayDeque<Char>>()
  private var index = 0

  override fun prepare() {
    index = 0
    stacks = MutableList(10) { ArrayDeque() }
    while (index < lines.size) {
      val line = lines[index++]
      if (line.isEmpty()) break
      if (line[1] == '1') continue
      for (i in 1..9) {
        val crate = line[(i - 1) * 4 + 1]
        if (crate == ' ') continue
        stacks[i].addLast(crate)
      }
    }
  }

  override fun solve(part1: Boolean): Any {
    prepare()
    for (line in lines.subList(index, lines.size)) {
      val tokens = line.split(' ')
      val num = tokens[1].toInt()
      val from = tokens[3].toInt()
      val to = tokens[5].toInt()
      if (part1) {
        repeat(num) { stacks[to].addFirst(stacks[from].removeFirst()) }
      } else {
        val temp = ArrayDeque<Char>()
        repeat(num) { temp.addFirst(stacks[from].removeFirst()) }
        repeat(num) { stacks[to].addFirst(temp.removeFirst()) }
      }
    }
    return stacks.filter(ArrayDeque<Char>::isNotEmpty).map(ArrayDeque<Char>::first).joinToString("")
  }
}

fun main() {
  Day5().run()
}
