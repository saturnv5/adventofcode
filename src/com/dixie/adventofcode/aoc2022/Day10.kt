package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import kotlin.math.abs

class Day10 : Day() {
  override fun part1(): Any {
    val vm = VM(lines)
    var cycle = 1
    return listOf(19, 40, 40, 40, 40, 40).map {
      repeat(it) { vm.executeCycle() }
      cycle += it
      vm.regX * cycle
    }.sum()
  }

  override fun part2(): Any {
    val vm = VM(lines)
    val screen = List(40 * 6) {
      val horX = it % 40
      val diff = vm.regX - horX
      vm.executeCycle()
      if (abs(diff) <= 1) '#'
      else ' '
    }.chunked(40)
    return screen.joinToString("\n", prefix = "\n") { it.joinToString("") }
  }
}

private class VM(val instructions: List<String>) {
  var regX = 1
  private var nextRegDelta = 0
  private var cyclesUntilNextUpdate = 0
  private var instructionIndex = 0

  fun executeCycle(): Boolean {
    if (cyclesUntilNextUpdate == 0) {
      if (instructionIndex == instructions.size) return false
      parseNextInstruction()
    }
    cyclesUntilNextUpdate--
    if (cyclesUntilNextUpdate == 0) {
      regX += nextRegDelta
    }
    return true
  }

  private fun parseNextInstruction() {
    val instruction = instructions[instructionIndex++]
    if (instruction == "noop") {
      nextRegDelta = 0
      cyclesUntilNextUpdate = 1
    } else {
      nextRegDelta = instruction.substring(5).toInt()
      cyclesUntilNextUpdate = 2
    }
  }
}

fun main() {
  Day10().run()
}
