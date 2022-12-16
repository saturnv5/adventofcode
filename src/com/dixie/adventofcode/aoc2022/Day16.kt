package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers
import com.dixie.adventofcode.lib.Memoizer

class Day16 : Day() {
  private lateinit var valves: Map<String, Valve>
  private val nonZeroValves = mutableSetOf<String>()

  private val maximumPressurePart1 = Memoizer.memoize(::maximumPressureReleasable)
  private val maximumPressurePart2 = Memoizer.memoize(::maximumPressureReleasableWithElephants)

  override fun prepare() {
    valves =
      lines
        .map {
          val tokens = it.split(' ')
          val label = tokens[1]
          val flowRate = tokens[4].extractNumbers().trim().toInt()
          val neighbours = (9 until tokens.size).map { index -> tokens[index].replace(",", "") }
          if (flowRate > 0) nonZeroValves += label
          Valve(label, flowRate, neighbours)
        }
        .associateBy(Valve::label)
  }

  override fun part1(): Any {
    return maximumPressurePart1.apply(State(setOf(), valves["AA"]!!, valves["AA"]!!, 0))
  }

  override fun part2(): Any {
    return maximumPressurePart2.apply(State(setOf(), valves["AA"]!!, valves["AA"]!!, 0))
  }

  private fun maximumPressureReleasable(state: State): Int {
    if (state.minutes == 30) return 0
    return state.nextStates(valves, nonZeroValves, false).maxOf(maximumPressurePart1::apply) +
      state.flowRate
  }

  private fun maximumPressureReleasableWithElephants(state: State): Int {
    if (state.minutes == 26) return 0
    return state.nextStates(valves, nonZeroValves, true).maxOf(maximumPressurePart2::apply) +
      state.flowRate
  }
}

private class Valve(val label: String, val flowRate: Int, val neighbours: List<String>)

private data class State(
  val openValves: Set<String>,
  val humanValve: Valve,
  val elephantValve: Valve,
  val minutes: Int
) {
  var flowRate = 0

  fun nextStates(
    valves: Map<String, Valve>,
    nonZeroValves: Set<String>,
    includeElephants: Boolean,
  ): List<State> {
    if (openValves.size == nonZeroValves.size) {
      // All non-zero valves already open.
      return listOf(
        State(openValves, humanValve, elephantValve, minutes + 1).apply {
          flowRate = this@State.flowRate
        }
      )
    }

    val states = mutableListOf<State>()

    if (!openValves.contains(humanValve.label) && nonZeroValves.contains(humanValve.label)) {
      // Open the current valve.
      val newOpenValves = mutableSetOf<String>()
      newOpenValves.addAll(openValves)
      newOpenValves += humanValve.label
      states +=
        State(newOpenValves, humanValve, elephantValve, minutes + 1).apply {
          flowRate = this@State.flowRate + humanValve.flowRate
        }
    }

    // Explore other valves through tunnels.
    humanValve.neighbours.forEach {
      states +=
        State(openValves, valves[it]!!, elephantValve, minutes + 1).apply {
          flowRate = this@State.flowRate
        }
    }

    if (includeElephants) {
      val elephantStates = mutableListOf<State>()
      states.forEach { state ->
        if (
          !state.openValves.contains(elephantValve.label) &&
            nonZeroValves.contains(elephantValve.label)
        ) {
          // Open the current valve.
          val newOpenValves = mutableSetOf<String>()
          newOpenValves.addAll(state.openValves)
          newOpenValves += elephantValve.label
          elephantStates +=
            State(newOpenValves, state.humanValve, elephantValve, state.minutes).apply {
              flowRate = state.flowRate + elephantValve.flowRate
            }
        }

        // Explore other valves through tunnels.
        elephantValve.neighbours.forEach {
          elephantStates +=
            State(state.openValves, state.humanValve, valves[it]!!, state.minutes).apply {
              flowRate = state.flowRate
            }
        }
      }
      return elephantStates
    }

    return states
  }
}

fun main() {
  Day16().run()
}
