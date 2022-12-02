package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day2 : Day() {
  private enum class Choice(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun resultAgainst(other: Choice): Result {
      if (this == other) return Result.DRAW
      val diff = Math.floorMod(this.score - other.score, 3)
      if (diff == 1) return Result.WIN else return Result.LOSS
    }
  }

  private enum class Result(val score: Int) {
    WIN(6),
    LOSS(0),
    DRAW(3)
  }

  private val oppoChoice = mapOf('A' to Choice.ROCK, 'B' to Choice.PAPER, 'C' to Choice.SCISSORS)
  private val playerChoice = mapOf('X' to Choice.ROCK, 'Y' to Choice.PAPER, 'Z' to Choice.SCISSORS)
  private val resultChoice = mapOf('X' to Result.LOSS, 'Y' to Result.DRAW, 'Z' to Result.WIN)

  override fun part1(): Any {
    return lines
      .map {
        val oppo = oppoChoice[it[0]]!!
        val player = playerChoice[it[2]]!!
        score(oppo, player)
      }
      .sum()
  }

  override fun part2(): Any {
    return lines
      .map {
        val oppo = oppoChoice[it[0]]!!
        val result = resultChoice[it[2]]!!
        score(oppo, choice(oppo, result))
      }
      .sum()
  }

  private fun score(oppo: Choice, player: Choice) = player.resultAgainst(oppo).score + player.score

  private fun choice(oppo: Choice, result: Result): Choice {
    val diff = when (result) {
      Result.WIN -> 1
      Result.LOSS -> -1
      Result.DRAW -> 0
    }
    val ordinal = Math.floorMod(oppo.ordinal + diff, 3)
    return Choice.values()[ordinal]
  }
}

fun main() {
  Day2().run()
}
