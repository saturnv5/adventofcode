package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day
import com.dixie.adventofcode.klib.extractNumbers
import com.dixie.adventofcode.klib.pmap
import kotlin.math.min
import kotlinx.coroutines.runBlocking

class Day19 : Day() {
  private lateinit var blueprints: List<Blueprint>

  override fun prepare() {
    blueprints =
      lines.map { line ->
        val numbers =
          line
            .extractNumbers()
            .replace('.', ' ')
            .split(" +".toRegex())
            .filter(String::isNotEmpty)
            .map(String::toInt)
        Blueprint(numbers[1], numbers[2], numbers[3] to numbers[4], numbers[5] to numbers[6])
      }
  }

  override fun part1(): Any {
    val maxGeodes = runBlocking {
      blueprints.pmap {
        maximumGeodesCollected(it, State(numOreRobots = 1, timeRemaining = 24), mutableMapOf())
      }
    }
    return maxGeodes.mapIndexed { i, geodes -> (i + 1) * geodes }.sum()
  }

  override fun part2(): Any {
    val maxGeodes = runBlocking {
      blueprints.take(3).pmap {
        maximumGeodesCollected(it, State(numOreRobots = 1, timeRemaining = 32), mutableMapOf())
      }
    }
    return maxGeodes.reduce { a, b -> a * b }
  }
}

private fun maximumGeodesCollected(
  blueprint: Blueprint,
  state: State,
  visited: MutableMap<State, Int>
): Int {
  if (state.timeRemaining == 1) return state.numGeodeRobots
  var best = visited[state]
  if (best == null) {
    best =
      state.nextStates(blueprint).maxOf { maximumGeodesCollected(blueprint, it, visited) } +
        state.numGeodeRobots
    visited[state] = best
  }
  return best
}

private class Blueprint(
  val oreRobotCost: Int,
  val clayRobotCost: Int,
  val obsidianRobotCost: Pair<Int, Int>,
  val geodeRobotCost: Pair<Int, Int>,
) {
  val maxOreCost =
    sequenceOf(oreRobotCost, clayRobotCost, obsidianRobotCost.first, geodeRobotCost.first).max()
  val maxClayCost = obsidianRobotCost.second
  val maxObsidianCost = geodeRobotCost.second
}

private data class State(
  val numOre: Int = 0,
  val numClay: Int = 0,
  val numObsidian: Int = 0,
  val numOreRobots: Int = 0,
  val numClayRobots: Int = 0,
  val numObsidianRobots: Int = 0,
  val numGeodeRobots: Int = 0,
  val timeRemaining: Int,
) {
  fun nextStates(blueprint: Blueprint): List<State> {
    val nextStates = mutableListOf<State>()

    // Always build geode robot if possible.
    buildGeodeRobot(blueprint)?.let(nextStates::add)
    if (nextStates.isEmpty()) {
      // Otherwise, try to build an obsidian robot.
      buildObsidianRobot(blueprint)?.let(nextStates::add)
      nextStates += doNothing()
    }
    if (nextStates.size == 1) {
      // Can't build either geode or obsidian robot, try simpler ones.
      buildOreRobot(blueprint)?.let(nextStates::add)
      buildClayRobot(blueprint)?.let(nextStates::add)
    }
    return nextStates.map { it.capResources(blueprint) }
  }

  fun capResources(blueprint: Blueprint) =
    // When there's enough robots, no need to keep excess resources.
    copy(
      numOre =
        if (numOreRobots >= blueprint.maxOreCost) min(numOre, blueprint.maxOreCost) else numOre,
      numClay =
        if (numClayRobots >= blueprint.maxClayCost) min(numClay, blueprint.maxClayCost)
        else numClay,
      numObsidian =
        if (numObsidianRobots >= blueprint.maxObsidianCost)
          min(numObsidian, blueprint.maxObsidianCost)
        else numObsidian,
    )

  private fun doNothing() =
    copy(
      numOre = numOre + numOreRobots,
      numClay = numClay + numClayRobots,
      numObsidian = numObsidian + numObsidianRobots,
      timeRemaining = timeRemaining - 1,
    )

  private fun buildOreRobot(blueprint: Blueprint): State? {
    if (blueprint.maxOreCost <= numOreRobots) return null // No need to build more.
    if (blueprint.oreRobotCost <= numOre) {
      return copy(
        numOre = numOre - blueprint.oreRobotCost + numOreRobots,
        numClay = numClay + numClayRobots,
        numObsidian = numObsidian + numObsidianRobots,
        numOreRobots = numOreRobots + 1,
        timeRemaining = timeRemaining - 1,
      )
    }
    return null
  }

  private fun buildClayRobot(blueprint: Blueprint): State? {
    if (blueprint.maxClayCost <= numClayRobots) return null // No need to build more.
    if (blueprint.clayRobotCost <= numOre) {
      return copy(
        numOre = numOre - blueprint.clayRobotCost + numOreRobots,
        numClay = numClay + numClayRobots,
        numObsidian = numObsidian + numObsidianRobots,
        numClayRobots = numClayRobots + 1,
        timeRemaining = timeRemaining - 1,
      )
    }
    return null
  }

  private fun buildObsidianRobot(blueprint: Blueprint): State? {
    if (blueprint.maxObsidianCost <= numObsidianRobots) return null // No need to build more.
    if (
      blueprint.obsidianRobotCost.first <= numOre && blueprint.obsidianRobotCost.second <= numClay
    ) {
      return copy(
        numOre = numOre - blueprint.obsidianRobotCost.first + numOreRobots,
        numClay = numClay - blueprint.obsidianRobotCost.second + numClayRobots,
        numObsidian = numObsidian + numObsidianRobots,
        numObsidianRobots = numObsidianRobots + 1,
        timeRemaining = timeRemaining - 1,
      )
    }
    return null
  }

  private fun buildGeodeRobot(blueprint: Blueprint): State? {
    if (
      blueprint.geodeRobotCost.first <= numOre && blueprint.geodeRobotCost.second <= numObsidian
    ) {
      return copy(
        numOre = numOre - blueprint.geodeRobotCost.first + numOreRobots,
        numClay = numClay + numClayRobots,
        numObsidian = numObsidian - blueprint.geodeRobotCost.second + numObsidianRobots,
        numGeodeRobots = numGeodeRobots + 1,
        timeRemaining = timeRemaining - 1,
      )
    }
    return null
  }
}

fun main() {
  Day19().run()
}
