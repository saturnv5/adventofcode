package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day21 : Day() {

  override fun part1(): Any {
    val monkeys = mutableMapOf<String, Lazy<Long>>()
    lines.forEach { line ->
      val tokens = line.split(':', ' ').filter(String::isNotEmpty)
      val name = tokens[0]
      var operation =
        if (tokens[1][0].isDigit()) {
          lazy { tokens[1].toLong() }
        } else {
          when (tokens[2]) {
            "+" -> lazy { monkeys[tokens[1]]!!.value + monkeys[tokens[3]]!!.value }
            "-" -> lazy { monkeys[tokens[1]]!!.value - monkeys[tokens[3]]!!.value }
            "*" -> lazy { monkeys[tokens[1]]!!.value * monkeys[tokens[3]]!!.value }
            "/" -> lazy { monkeys[tokens[1]]!!.value / monkeys[tokens[3]]!!.value }
            else -> throw IllegalArgumentException("Unrecognised operation: ${tokens[2]}")
          }
        }
      monkeys[name] = operation
    }
    return monkeys["root"]!!.value
  }

  override fun part2(): Any {
    val monkeys = mutableMapOf<String, Lazy<String>>()
    lines.forEach { line ->
      val tokens = line.split(':', ' ').filter(String::isNotEmpty)
      val name = tokens[0]
      var operation =
        if (tokens[1][0].isDigit()) {
          lazy { tokens[1] }
        } else if (name == "root") {
          lazy { "${monkeys[tokens[1]]!!.value} = ${monkeys[tokens[3]]!!.value}" }
        } else {
          when (tokens[2]) {
            "+" -> lazy { "(${monkeys[tokens[1]]!!.value} + ${monkeys[tokens[3]]!!.value})" }
            "-" -> lazy { "(${monkeys[tokens[1]]!!.value} - ${monkeys[tokens[3]]!!.value})" }
            "*" -> lazy { "(${monkeys[tokens[1]]!!.value} * ${monkeys[tokens[3]]!!.value})" }
            "/" -> lazy { "(${monkeys[tokens[1]]!!.value} / ${monkeys[tokens[3]]!!.value})" }
            else -> throw IllegalArgumentException("Unrecognised operation: ${tokens[2]}")
          }
        }
      monkeys[name] = operation
    }
    monkeys["humn"] = lazy { "x" }
    return monkeys["root"]!!.value
  }
}

fun main() {
  Day21().run()
}
