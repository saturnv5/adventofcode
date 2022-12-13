package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

const val LEFT_BRACKET = -1
const val RIGHT_BRACKET = -2

class Day13 : Day() {

  override fun part1(): Any {
    val validIndices = mutableListOf<Int>()
    var index = 1
    val itr = lines.iterator()
    while (itr.hasNext()) {
      val packet1 = itr.next()
      val packet2 = itr.next()
      if (itr.hasNext()) itr.next()

      if (packetsInCorrectOrder(tokenise(packet1), tokenise(packet2))) {
        validIndices += index
      }
      index++
    }
    return validIndices.sum()
  }

  override fun part2(): Any {
    val packets = lines.filter(String::isNotEmpty).map(::tokenise).toMutableList()
    val divider1 = tokenise("[[2]]")
    val divider2 = tokenise("[[6]]")
    packets += tokenise("[[2]]")
    packets += tokenise("[[6]]")

    packets.sortWith { p1, p2 -> if (packetsInCorrectOrder(p1, p2)) -1 else 1 }
    val divider1Index = packets.indexOf(divider1) + 1
    val divider2Index = packets.indexOf(divider2) + 1
    return divider1Index * divider2Index
  }

  private fun packetsInCorrectOrder(packet1: List<Int>, packet2: List<Int>): Boolean {
    val p1 = mutableListOf<Int>()
    p1.addAll(packet1)
    val p2 = mutableListOf<Int>()
    p2.addAll(packet2)
    var i = 0
    var j = 0
    while (i < p1.size && j < p2.size) {
      val token1 = p1[i++]
      val token2 = p2[j++]
      if (token1 == token2) continue
      if (token1 >= 0 && token2 >= 0) {
        // Both are numbers.
        if (token1 < token2) return true else if (token1 > token2) return false
      } else if (token1 == RIGHT_BRACKET) {
        // First list was shorter.
        return true
      } else if (token2 == RIGHT_BRACKET) {
        // Second list was shorter.
        return false
      } else if (token1 == LEFT_BRACKET) {
        // First token is a list, convert second token to a singleton list.
        // Add a closing bracket to the second packet and move back one token.
        p2.add(j, RIGHT_BRACKET)
        j--
      } else if (token2 == LEFT_BRACKET) {
        // Second token is a list, convert first token to a singleton list.
        // Add a closing bracket to the first packet and move back one token.
        p1.add(i, RIGHT_BRACKET)
        i--
      } else {
        check(false) { "Unexpected edge case!" }
      }
    }
    return false
  }

  private fun tokenise(packet: String): List<Int> {
    val tokens = mutableListOf<Int>()
    packet.split(',').forEach {
      var item = it
      // Tokenise opening brackets.
      while (item.first() == '[') {
        tokens += LEFT_BRACKET
        item = item.substring(1)
      }
      // Tokenise number.
      if (item.first() != ']') {
        tokens += item.substringBefore(']').toInt()
      }
      // Tokenise closing brackets.
      while (item.isNotEmpty() && item.last() == ']') {
        tokens += RIGHT_BRACKET
        item = item.substring(0, item.length - 1)
      }
    }
    return tokens
  }
}

fun main() {
  Day13().run()
}
