package com.dixie.adventofcode.aoc2023

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers

class Day2 : Day() {
  private lateinit var games: List<Game>

  override fun prepare() {
    games =
      lines.mapIndexed { index, line ->
        val samples = line.substring(line.indexOf(':')).split(";")
        Game(
          index + 1,
          samples.map { sample ->
            var red = 0
            var green = 0
            var blue = 0
            sample.split(",").forEach {
              if (it.contains("red")) red = it.extractNumbers().toInt()
              else if (it.contains("green")) green = it.extractNumbers().toInt()
              else blue = it.extractNumbers().toInt()
            }
            Sample(red, green, blue)
          }
        )
      }
  }

  override fun part1() =
    games
      .filter { game ->
        game.samples.none { it.red > TOTAL_RED || it.green > TOTAL_GREEN || it.blue > TOTAL_BLUE }
      }
      .sumOf(Game::id)

  override fun part2() =
    games.sumOf { game ->
      game.samples.maxOf(Sample::red) *
        game.samples.maxOf(Sample::green) *
        game.samples.maxOf(Sample::blue)
    }

  data class Game(val id: Int, val samples: List<Sample>)
  data class Sample(val red: Int, val green: Int, val blue: Int)

  companion object {
    const val TOTAL_RED = 12
    const val TOTAL_GREEN = 13
    const val TOTAL_BLUE = 14
  }
}

fun main() {
  Day2().run()
}
