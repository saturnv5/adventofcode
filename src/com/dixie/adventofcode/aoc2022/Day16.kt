package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.Graph
import com.dixie.adventofcode.klib.extractNumbers

class Day16 : Day() {
  private lateinit var startingValve: Valve
  private val nonZeroValves = mutableSetOf<Valve>()
  private val travelTimes = mutableMapOf<Valve, List<Pair<Valve, Int>>>()

  override fun prepare() {
    val valves =
      lines
        .map {
          val tokens = it.split(' ')
          val label = tokens[1]
          val flowRate = tokens[4].extractNumbers().trim().toInt()
          val neighbours = (9 until tokens.size).map { index -> tokens[index].replace(",", "") }
          val valve = Valve(label, flowRate, neighbours)
          if (flowRate > 0) nonZeroValves += valve
          return@map valve
        }
        .associateBy(Valve::label)

    startingValve = valves["AA"]!!

    val graph =
      Graph.unweighted { valve: Valve -> valve.neighbours.asSequence().mapNotNull(valves::get) }

    for (origin in nonZeroValves union setOf(startingValve)) {
      val destinationTimes = mutableListOf<Pair<Valve, Int>>()
      for (destination in nonZeroValves) {
        destinationTimes +=
          if (origin == destination) {
            destination to 0
          } else {
            destination to graph.findShortestPath(origin, destination)!!.cost.toInt()
          }
      }
      travelTimes[origin] = destinationTimes
    }
  }

  override fun part1(): Any {
    return maximumPressureReleased(startingValve, startingValve, setOf(), 30, 0)
  }

  override fun part2(): Any {
    return maximumPressureReleased(startingValve, startingValve, setOf(), 26, 26)
  }

  private fun maximumPressureReleased(
    humanValve: Valve,
    elephantValve: Valve,
    openedValves: Set<Valve>,
    humanTimeRemaining: Int,
    elephantTimeRemaining: Int,
  ): Int {
    if (humanTimeRemaining == 0) return 0
    if (openedValves.size == nonZeroValves.size) return 0

    return travelTimes[humanValve]!!
      .filterNot { (v, t) -> openedValves.contains(v) || t >= humanTimeRemaining }
      .maxOfOrNull { (nextHumanValve, humanTravelTime) ->
        val newHumanTimeRemaining = humanTimeRemaining - humanTravelTime - 1
        val pressureReleased = nextHumanValve.flowRate * newHumanTimeRemaining

        pressureReleased +
          (travelTimes[elephantValve]!!
            .filterNot { (v, t) ->
              nextHumanValve == v || openedValves.contains(v) || t >= elephantTimeRemaining
            }
            .maxOfOrNull { (nextElephantValve, elephantTravelTime) ->
              val newElephantTimeRemaining = elephantTimeRemaining - elephantTravelTime - 1
              val pressureReleased = nextElephantValve.flowRate * newElephantTimeRemaining

              pressureReleased +
                maximumPressureReleased(
                  nextHumanValve,
                  nextElephantValve,
                  openedValves union setOf(nextHumanValve, nextElephantValve),
                  newHumanTimeRemaining,
                  newElephantTimeRemaining
                )
            }
            ?: maximumPressureReleased(
              nextHumanValve,
              elephantValve,
              openedValves union setOf(nextHumanValve),
              newHumanTimeRemaining,
              elephantTimeRemaining
            ))
      }
      ?: 0
  }
}

private class Valve(val label: String, val flowRate: Int, val neighbours: List<String>)

fun main() {
  Day16().run()
}
