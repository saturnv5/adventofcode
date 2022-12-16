package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers
import java.util.PriorityQueue
import kotlin.math.max

class Day16 : Day() {
  private lateinit var valves: Map<String, Valve>
  private val nonZeroValves = mutableSetOf<String>()

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
    val fringe = PriorityQueue<State> { s1, s2 ->
      if (s1.totalPressureReleased == s2.totalPressureReleased) {
        if (s1.flowRate == s2.flowRate) {
          return@PriorityQueue s1.minutes.compareTo(s2.minutes)
        }
        return@PriorityQueue s2.flowRate.compareTo(s1.flowRate)
      }
      return@PriorityQueue s2.totalPressureReleased.compareTo(s1.totalPressureReleased)
    }

    fringe.offer(State(setOf(), 0, 0, valves["AA"]!!, 0))
    val visited = mutableSetOf<State>()

    var maxPressureReleased = 0

    while (fringe.isNotEmpty()) {
      val currentState = fringe.poll()
      if (!visited.add(currentState)) continue

      if (currentState.minutes == 29) {
        maxPressureReleased = max(maxPressureReleased, currentState.totalPressureReleased + 1 * currentState.flowRate)
        continue
      }

      currentState
        .nextStates(valves, nonZeroValves)
        .filterNot(visited::contains)
        .forEach(fringe::offer)
    }

    return maxPressureReleased
  }
}

private class Valve(val label: String, val flowRate: Int, val neighbours: List<String>)

private data class State(
  val openValves: Set<String>,
  val flowRate: Int,
  val totalPressureReleased: Int,
  val currentValve: Valve,
  val minutes: Int
) {

  fun nextStates(valves: Map<String, Valve>, nonZeroValves: Set<String>): List<State> {
    val states = mutableListOf<State>()

    if (openValves.size == nonZeroValves.size) {
      // All non-zero valves already open.
      states +=
        State(openValves, flowRate, totalPressureReleased + flowRate, currentValve, minutes + 1)
    }

    if (!openValves.contains(currentValve.label) && nonZeroValves.contains(currentValve.label)) {
      // Open the current valve.
      val newOpenValves = mutableSetOf<String>()
      newOpenValves.addAll(openValves)
      newOpenValves += currentValve.label
      states +=
        State(
          newOpenValves,
          flowRate + currentValve.flowRate,
          totalPressureReleased + flowRate,
          currentValve,
          minutes + 1
        )
    }

    // Explore other valves through tunnels.
    currentValve.neighbours.forEach {
      states +=
        State(openValves, flowRate, totalPressureReleased + flowRate, valves[it]!!, minutes + 1)
    }

    return states
  }
}

fun main() {
  Day16().run()
}
