package com.dixie.adventofcode.klib

abstract class Day {
  protected lateinit var lines: List<String>

  fun run() {
    println("Input:")
    lines = generateSequence { readlnOrNull() }.takeWhile { it != null && it != "end" }.toList()
    prepare()
    println("Part 1: ${solve(true)}")
    println("Part 2: ${solve(false)}")
  }

  open fun prepare() {}

  open fun solve(part1: Boolean) = if (part1) part1() else part2()

  open fun part1(): Any = "N/A"

  open fun part2(): Any = "N/A"
}

fun String.toInts(delimiter: String = " ") = splitToSequence(delimiter).map(String::toInt)
fun String.toLongs(delimiter: String = " ") = splitToSequence(delimiter).map(String::toLong)
fun String.toDoubles(delimiter: String = " ") = splitToSequence(delimiter).map(String::toDouble)
fun List<String>.toInts() = asSequence().map(String::toInt)
fun List<String>.toLongs() = asSequence().map(String::toLong)
fun List<String>.toDoubles() = asSequence().map(String::toDouble)