package com.dixie.adventofcode.lib;

import com.google.common.graph.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class GraphUtils {

  public static <N> Path<N> shortestPath(Graph<N> graph, N origin, N destination) {
    return shortestPath(n -> graph.successors(n)
            .stream()
            .map(m -> Pair.of(m, 1L))
            .toList(), origin, destination);
  }

  public static <N> Path<N> shortestPath(ValueGraph<N, Long> graph, N origin, N destination) {
    return shortestPath(successorFromValueGraph(graph), origin, destination);
  }

  public static <N> Path<N> shortestPath(
      Function<N, Iterable<Pair<N, Long>>> successors, N origin, N destination) {
    PriorityQueue<SearchNode<N>> fringe = new PriorityQueue<>();
    HashSet<N> visited = new HashSet<>();
    fringe.offer(new SearchNode<>(origin, null, 0));

    while (!fringe.isEmpty()) {
      SearchNode<N> current = fringe.poll();
      if (current.node.equals(destination)) {
        return new Path<>(current.constructPath(), current.cost);
      }
      if (!visited.add(current.node)) {
        continue;
      }
      for (Pair<N, Long> next : successors.apply(current.node)) {
        if (!visited.contains(next.first)) {
          fringe.offer(new SearchNode<>(next.first, current, current.cost + next.second));
        }
      }
    }
    return null; // Not found.
  }

  public static <N, E> Function<N, Iterable<Pair<N, E>>> successorFromValueGraph(
      ValueGraph<N, E> graph) {
    return n -> graph.successors(n)
        .stream()
        .map(m -> Pair.of(m, graph.edgeValue(n, m).get()))
        .toList();
  }

  private static class SearchNode<N> implements Comparable<SearchNode<N>> {
    final N node;
    final SearchNode<N> predeccessor;
    final long cost;

    SearchNode(N node, SearchNode<N> predeccessor, long cost) {
      this.node = node;
      this.predeccessor = predeccessor;
      this.cost = cost;
    }

    List<N> constructPath() {
      ArrayList<N> path = new ArrayList<>();
      SearchNode<N> n = this;
      while (n != null) {
        path.add(n.node);
        n = n.predeccessor;
      }
      Collections.reverse(path);
      return path;
    }

    @Override
    public int compareTo(SearchNode<N> o) {
      return Long.compare(cost, o.cost);
    }
  }
}
