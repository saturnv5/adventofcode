package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day

class Day7 : Day() {
  private enum class Type {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
  }

  override fun solve(part1: Boolean): Any {
    var handsWithBets =
      lines.map { line ->
        val tokens = line.split(" ")
        Hand(tokens[0], !part1) to tokens[1].toInt()
      }
    handsWithBets = handsWithBets.sortedBy { handWithBet -> handWithBet.first }
    return handsWithBets.mapIndexed { i, handWithBet -> (i + 1) * handWithBet.second }.sum()
  }

  private class Hand(hand: String, hasJokers: Boolean) : Comparable<Hand> {
    val hand: String
    val type: Type

    init {
      this.hand =
        hand
          .map { ch ->
            when (ch) {
              'T' -> 'A'
              'J' -> if (hasJokers) '0' else 'B'
              'Q' -> 'C'
              'K' -> 'D'
              'A' -> 'E'
              else -> ch
            }
          }
          .joinToString("")
      var handGroups = hand.groupBy { it }
      if (hasJokers && handGroups.size > 1) {
        val best =
          handGroups
            .filter { entry -> entry.key != 'J' }
            .maxByOrNull { entry -> entry.value.size }!!
            .key
        handGroups = hand.replace('J', best).groupBy { it }
      }
      type =
        if (handGroups.size == 1) Type.FIVE_OF_A_KIND
        else if (handGroups.size == 5) Type.HIGH_CARD
        else if (handGroups.size == 4) Type.ONE_PAIR
        else if (handGroups.size == 2) {
          if (handGroups.values.any { it.size == 4 }) Type.FOUR_OF_A_KIND else Type.FULL_HOUSE
        } else {
          if (handGroups.values.any { it.size == 3 }) Type.THREE_OF_A_KIND else Type.TWO_PAIR
        }
    }

    override fun compareTo(other: Hand): Int {
      if (type == other.type) {
        return hand.compareTo(other.hand)
      }
      return type.ordinal - other.type.ordinal
    }
  }
}

fun main() {
  Day7().run()
}
