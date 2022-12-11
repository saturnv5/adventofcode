package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day11 : Day() {
  private lateinit var monkeys: List<Monkey>

  override fun prepare() {
    // Parse input
    val monkeys = mutableListOf<Monkey>()

    for (i in lines.indices step 7) {
      val items = lines[i + 1].substringAfter("items: ").split(", ").map(String::toLong)
      val opTokens = lines[i + 2].substringAfter("new = old ").split(' ')
      val op: (Long) -> Long =
        if (opTokens[0] == "+") {
          if (opTokens[1] == "old") {
            { it + it }
          } else {
            { it + opTokens[1].toLong() }
          }
        } else {
          if (opTokens[1] == "old") {
            { it * it }
          } else {
            { it * opTokens[1].toLong() }
          }
        }
      val divisor = lines[i + 3].substringAfter("divisible by ").toLong()
      val throwIfTrue = lines[i + 4].substringAfter("monkey ").toInt()
      val throwIfFalse = lines[i + 5].substringAfter("monkey ").toInt()

      monkeys += Monkey(ArrayDeque(items), op, divisor, throwIfTrue, throwIfFalse)
    }

    this.monkeys = monkeys
  }

  override fun part1(): Any {
    var monkeys = this.monkeys.map(Monkey::clone)
    repeat(20) { monkeys.forEach { it.inspectItems(monkeys, true) } }
    monkeys = monkeys.sortedByDescending(Monkey::numInspections)
    return monkeys[0].numInspections * monkeys[1].numInspections
  }

  override fun part2(): Any {
    var monkeys = this.monkeys.map(Monkey::clone)
    val lcm = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisor }
    repeat(10_000) { monkeys.forEach { it.inspectItems(monkeys, false, lcm) } }
    monkeys = monkeys.sortedByDescending(Monkey::numInspections)
    return monkeys[0].numInspections * monkeys[1].numInspections
  }
}

private data class Monkey(
  val items: ArrayDeque<Long>,
  val op: (Long) -> Long,
  val divisor: Long,
  val throwIfTrue: Int,
  val throwIfFalse: Int
) {
  var numInspections = 0L

  fun inspectItems(monkeys: List<Monkey>, reliefAfterInspection: Boolean, lcm: Long = 0) {
    while (items.isNotEmpty()) inspectNextItem(monkeys, reliefAfterInspection, lcm)
  }

  private fun inspectNextItem(monkeys: List<Monkey>, reliefAfterInspection: Boolean, lcm: Long) {
    val itemWorry = items.removeFirst()
    var newWorry = op(itemWorry)
    if (reliefAfterInspection) newWorry /= 3
    else newWorry %= lcm
    if (newWorry % divisor == 0L) monkeys[throwIfTrue].items.addLast(newWorry)
    else monkeys[throwIfFalse].items.addLast(newWorry)
    numInspections++
  }

  fun clone() = copy(items = ArrayDeque(items))
}

fun main() {
  Day11().run()
}
