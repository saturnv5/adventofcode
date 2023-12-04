package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers
import com.dixie.adventofcode.klib.toInts
import com.dixie.adventofcode.lib.Memoizer

class Day4 : Day() {
  private lateinit var matches: List<Int>

  override fun prepare() {
    matches =
      lines.map { line ->
        val (_, winnings, selected) = line.split(':', '|')
        val winningNumbers = winnings.extractNumbers().toInts(" ").toSet()
        val selectedNumbers = selected.extractNumbers().toInts(" ").toSet()
        winningNumbers.intersect(selectedNumbers).size
      }
  }

  override fun part1() = matches.sumOf { if (it == 0) 0 else 1 shl (it - 1) }

  override fun part2() = matches.indices.sumOf(memoizedCardCounts::apply)

  private val memoizedCardCounts = Memoizer.memoize(::cardCount)

  private fun cardCount(index: Int): Int {
    return 1 + ((index + 1) .. (index + matches[index])).sumOf(memoizedCardCounts::apply)
  }
}

fun main() {
  Day4().run()
}
