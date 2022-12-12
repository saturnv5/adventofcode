package com.dixie.adventofcode.klib

import java.util.Comparator
import java.util.PriorityQueue

class Graph<N : Any> {
  private val successors: (N) -> Sequence<Pair<N, Long>>

  private constructor(successors: (N) -> Sequence<Pair<N, Long>>) {
    this.successors = successors
  }

  fun findShortedPath(origin: N, destination: N, visitor: Visitor<N> = DefaultVisitor<N>()) =
    findShortedPath(origin, destination::equals, visitor)

  fun findShortedPath(
    origin: N,
    destination: (N) -> Boolean,
    visitor: Visitor<N> = DefaultVisitor<N>()
  ): Path<N>? {
    val fringe = PriorityQueue(Comparator.comparing(SearchNode<N>::cost))
    fringe.add(SearchNode(origin, null, 0))
    while (fringe.isNotEmpty()) {
      val cur = fringe.poll()
      if (!visitor.visit(cur.value)) continue
      if (destination(cur.value)) return cur.constructPath()

      successors(cur.value)
        .filterNot { (next, _) -> visitor.hasVisited(next) }
        .map { (next, weight) -> SearchNode(next, cur, cur.cost + weight) }
        .forEach(fringe::offer)
    }
    return null
  }

  companion object {
    fun <N : Any> weighted(successors: (N) -> Sequence<Pair<N, Long>>) = Graph(successors)
    fun <N : Any> unweighted(successors: (N) -> Sequence<N>) = Graph { n: N ->
      successors(n).map { it to 1L }
    }
  }
}

interface Visitor<N> {
  fun hasVisited(n: N): Boolean
  fun visit(n: N): Boolean
}

class Path<N>(val nodes: List<N>, val cost: Long)

private class SearchNode<N>(val value: N, val predecessor: SearchNode<N>?, val cost: Long) {
  fun constructPath(): Path<N> {
    val nodes =
      generateSequence(this, SearchNode<N>::predecessor)
        .map(SearchNode<N>::value)
        .toList()
        .asReversed()
    return Path(nodes, cost)
  }
}

private class DefaultVisitor<N> : Visitor<N> {
  private val visited = mutableSetOf<N>()

  override fun hasVisited(n: N) = visited.contains(n)
  override fun visit(n: N) = visited.add(n)
}
