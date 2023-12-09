package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.lib.MathUtils

class Day8 : Day() {
  private lateinit var instructions: String
  private lateinit var nodes: Map<String, Pair<String, String>>

  override fun prepare() {
    instructions = lines[0]
    val nodeRegex = Regex("[A-Z]+")
    nodes =
      lines.drop(2).associate { line ->
        val tokens = nodeRegex.findAll(line).map(MatchResult::value).toList()
        tokens[0] to (tokens[1] to tokens[2])
      }
  }

  override fun part1() = steps("AAA") { it == "ZZZ" }

  override fun part2() =
    nodes.keys
      .asSequence()
      .filter { it.last() == 'A' }
      .map { start -> steps(start) { it.last() == 'Z' } }.toList()
      .reduce(MathUtils::lcm)

  private fun steps(start: String, end: (String) -> Boolean): Long {
    var i = 0
    var node = start
    var steps = 0L
    while (!end(node)) {
      node = if (instructions[i++] == 'L') nodes[node]!!.first else nodes[node]!!.second
      if (i == instructions.length) i = 0
      steps++
    }
    return steps
  }
}

fun main() {
  Day8().run()
}
