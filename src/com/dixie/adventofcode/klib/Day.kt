package com.dixie.adventofcode.klib

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.max
import kotlin.math.min

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

fun String.toInts(delimiter: String = " ") =
  splitToSequence(delimiter).filter(String::isNotEmpty).map(String::toInt)

fun String.toLongs(delimiter: String = " ") =
  splitToSequence(delimiter).filter(String::isNotEmpty).map(String::toLong)

fun String.toDoubles(delimiter: String = " ") =
  splitToSequence(delimiter).filter(String::isNotEmpty).map(String::toDouble)

fun String.extractNumbers() = filter { it.isDigit() || it == '.' || it == '-' }

fun List<String>.toInts() = asSequence().map(String::toInt)

fun List<String>.toLongs() = asSequence().map(String::toLong)

fun List<String>.toDoubles() = asSequence().map(String::toDouble)

inline fun <T> List<String>.toMatrix(transformer: (Char) -> T) =
  map { it.map(transformer) }.toList()

fun List<List<*>>.matrixIndices() =
  indices.asSequence().flatMap { row -> this[row].indices.asSequence().map { col -> row to col } }

fun List<List<*>>.matrixPositions() =
  indices.asSequence().flatMap { y -> this[y].indices.asSequence().map { x -> x to y } }

enum class Direction(val dx: Int, val dy: Int) {
  UP(0, -1),
  RIGHT(1, 0),
  DOWN(0, 1),
  LEFT(-1, 0);

  fun turnLeft() = Direction.values()[(ordinal + 3) % Direction.values().size]
  fun turnRight() = Direction.values()[(ordinal + 1) % Direction.values().size]

  fun turnAround() = Direction.values()[(ordinal + 2) % Direction.values().size]
}

fun Pair<Int, Int>.move(dir: Direction, dist: Int = 1) =
  (first + dir.dx * dist) to (second + dir.dy * dist)

val Pair<Int, Int>.x: Int
  get() = first

val Pair<Int, Int>.y: Int
  get() = second

fun clamp(num: Int, min: Int, max: Int) = max(min, min(max, num))

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
  map { async { f(it) } }.awaitAll()
}