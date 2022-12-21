package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day21 : Day() {
  private val monkeys = mutableMapOf<String, BiOp>()

  override fun prepare() {
    lines.forEach { line ->
      val tokens = line.split(':', ' ').filter(String::isNotEmpty)
      val name = tokens[0]
      val operation =
        if (tokens[1][0].isDigit()) {
          BiOp(value = tokens[1].toLong())
        } else {
          val op =
            when (tokens[2]) {
              "+" -> Op.ADD
              "-" -> Op.SUB
              "*" -> Op.MUL
              "/" -> Op.DIV
              else -> throw IllegalArgumentException("Unrecognised operation: ${tokens[2]}")
            }
          BiOp({ monkeys[tokens[1]] }, op, { monkeys[tokens[3]] })
        }
      monkeys[name] = operation
    }
  }

  override fun part1(): Any {
    return monkeys["root"]?.evaluate()!!
  }

  override fun part2(): Any {
    monkeys["humn"] = BiOp(value = null)

    val root = monkeys["root"]!!
    val leftResult = root.lhs()?.evaluate()
    val rightResult = root.rhs()?.evaluate()

    return if (rightResult != null) root.lhs()?.solveMissingValue(rightResult)!!
    else if (leftResult != null) root.rhs()?.solveMissingValue(leftResult)!!
    else "Could not solve for X!"
  }

  private class BiOp(
    val lhs: () -> BiOp? = { null },
    val op: Op = Op.NONE,
    val rhs: () -> BiOp? = { null },
    val value: Long? = null,
  ) {
    fun evaluate(): Long? {
      if (value != null) return value
      val leftResult = lhs()?.evaluate()
      val rightResult = rhs()?.evaluate()
      if (leftResult == null || rightResult == null) return null
      return when (op) {
        Op.ADD -> leftResult + rightResult
        Op.SUB -> leftResult - rightResult
        Op.MUL -> leftResult * rightResult
        Op.DIV -> leftResult / rightResult
        else -> null
      }
    }

    fun solveMissingValue(target: Long): Long? {
      if (lhs() == null && rhs() == null) return target
      val leftResult = lhs()?.evaluate()
      val rightResult = rhs()?.evaluate()
      if (leftResult == null && rightResult == null) return null
      else if (rightResult != null) {
        return when (op) {
          Op.ADD -> lhs()?.solveMissingValue(target - rightResult)
          Op.SUB -> lhs()?.solveMissingValue(target + rightResult)
          Op.MUL -> lhs()?.solveMissingValue(target / rightResult)
          Op.DIV -> lhs()?.solveMissingValue(target * rightResult)
          else -> null
        }
      } else if (leftResult != null) {
        return when (op) {
          Op.ADD -> rhs()?.solveMissingValue(target - leftResult)
          Op.SUB -> rhs()?.solveMissingValue(leftResult - target)
          Op.MUL -> rhs()?.solveMissingValue(target / leftResult)
          Op.DIV -> rhs()?.solveMissingValue(leftResult / target)
          else -> null
        }
      }
      return null
    }
  }

  private enum class Op {
    ADD,
    SUB,
    MUL,
    DIV,
    NONE
  }
}

fun main() {
  Day21().run()
}
