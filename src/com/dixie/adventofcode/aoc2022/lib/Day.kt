package com.dixie.adventofcode.aoc2022.lib

abstract class Day {
  protected lateinit var lines: List<String>

  fun run() {
    println("Input:")
    val lines = generateSequence { readlnOrNull() }.takeWhile { it != null && it != "end" }.toList()
    prepare()
    println("Part 1: ${solve(true)}")
    println("Part 2: ${solve(false)}")
  }

  abstract fun prepare()

  open fun solve(part1: Boolean) = if (part1) part1() else part2()

  abstract fun part1(): String

  abstract fun part2(): String
}

fun String.toInts(delimiter: String = " ") = splitToSequence(delimiter).map(String::toInt)
fun String.toLongs(delimiter: String = " ") = splitToSequence(delimiter).map(String::toLong)
fun String.toDoubles(delimiter: String = " ") = splitToSequence(delimiter).map(String::toDouble)