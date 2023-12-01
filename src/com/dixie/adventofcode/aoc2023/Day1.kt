package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day

class Day1 : Day() {

  override fun part1() =
    lines
      .map { line ->
        val firstDigit = line.find(Char::isDigit)!!.digitToInt()
        val lastDigit = line.findLast(Char::isDigit)!!.digitToInt()
        firstDigit * 10 + lastDigit
      }
      .sum()

  override fun part2() =
    lines
      .map { line ->
        val (_, firstMatch) = line.findAnyOf(WORDS_AND_DIGITS)!!
        val (_, lastMatch) = line.findLastAnyOf(WORDS_AND_DIGITS)!!
        val firstDigit = convertToInt(firstMatch)
        val lastDigit = convertToInt(lastMatch)
        firstDigit * 10 + lastDigit
      }
      .sum()

  private fun convertToInt(wordOrDigit: String) =
    wordOrDigit.toIntOrNull() ?: WORD_TO_INT[wordOrDigit]!!

  companion object {
    val WORD_TO_INT =
      mapOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
      )
    val WORDS = WORD_TO_INT.keys
    val DIGITS = WORD_TO_INT.values.map(Int::toString)
    val WORDS_AND_DIGITS = WORDS + DIGITS
  }
}

fun main() {
  Day1().run()
}
