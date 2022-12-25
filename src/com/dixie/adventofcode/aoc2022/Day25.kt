package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day25 : Day() {
  private val DIGITS = mapOf('2' to 2, '1' to 1, '0' to 0, '-' to -1, '=' to -2)

  override fun part1(): Any {
    return toSnafu(lines.map(::toDecimal).sum())
  }

  private fun toDecimal(snafu: String): Long {
    var place = 1L
    var decimal = 0L
    for (i in snafu.indices.reversed()) {
      val faceVal = DIGITS[snafu[i]]!!
      decimal += faceVal * place
      place *= 5L
    }
    return decimal
  }

  private fun toSnafu(decimal: Long): String {
    val quinary = decimal.toString(5)
    val snafu = mutableListOf<Char>()
    var carry = 0
    for (i in quinary.indices.reversed()) {
      var digit = quinary[i].digitToInt() + carry
      carry = digit / 5
      digit %= 5
      if (digit < 3) snafu += digit.digitToChar()
      else {
        if (digit == 3) snafu += '='
        else if (digit == 4) snafu += '-'
        carry++
      }
    }
    snafu.reverse()
    return snafu.joinToString("")
  }
}

fun main() {
  Day25().run()
}
