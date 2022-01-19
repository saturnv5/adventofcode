package com.dixie.adventofcode.lib;

import com.google.common.collect.Streams;
import com.google.common.graph.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

  public static <N> Path<N> shortestNonWeightedPath(
      Function<N, Stream<N>> successors, N origin, N destination) {
    return shortestNonWeightedPath(successors, origin, Predicate.isEqual(destination));
  }

  public static <N> Path<N> shortestNonWeightedPath(
      Function<N, Stream<N>> successors, N origin, Predicate<N> endCondition) {
    return shortestNonWeightedPath(successors, origin, new DefaultVisitor<>(), endCondition);
  }

  public static <N> Path<N> shortestNonWeightedPath(Function<N, Stream<N>> successors, N origin,
      Visitor<N> visitor, Predicate<N> endCondition) {
    return shortestPath(
        successors.andThen(s -> s.map(n -> Pair.of(n, 1L)).toList()), origin, visitor, endCondition);
  }

  public static <N> Path<N> shortestPath(
      Function<N, Iterable<Pair<N, Long>>> successors, N origin, N destination) {
    return shortestPath(successors, origin, Predicate.isEqual(destination));
  }

  public static <N> Path<N> shortestPath(
      Function<N, Iterable<Pair<N, Long>>> successors, N origin, Predicate<N> endCondition) {
    return shortestPath(successors, origin, new DefaultVisitor<>(), endCondition);
  }

  public static <N> Path<N> shortestPath(Function<N, Iterable<Pair<N, Long>>> successors, N origin,
      Visitor<N> visitor, Predicate<N> endCondition) {
    PriorityQueue<SearchNode<N>> fringe = new PriorityQueue<>();
    fringe.offer(new SearchNode<>(origin, null, 0));

    while (!fringe.isEmpty()) {
      SearchNode<N> current = fringe.poll();
      if (endCondition.test(current.node)) {
        return new Path<>(current.constructPath(), current.cost);
      }
      if (!visitor.visit(current.node)) {
        continue;
      }
      for (Pair<N, Long> next : successors.apply(current.node)) {
        if (!visitor.hasVisited(next.first)) {
          fringe.offer(new SearchNode<>(next.first, current, current.cost + next.second));
        }
      }
    }
    return null; // Not found.
  }

  public static <N> Path<N> longestBfsPath(Function<N, Stream<N>> successors, N origin) {
    AtomicReference<SearchNode<N>> lastNode = new AtomicReference<>();
    bft(successors, origin, n -> lastNode.set(n));
    return lastNode.get() == null
        ? null
        : new Path<>(lastNode.get().constructPath(), lastNode.get().cost);
  }

  public static <N> void breathFirstTraversal(Graph<N> graph, N origin, Consumer<N> consumer) {
    bft(n -> graph.successors(n).stream(), origin, n -> consumer.accept(n.node));
  }

  public static <N> void breathFirstTraversal(
      Function<N, Stream<N>> successors, N origin, Consumer<N> consumer) {
    bft(successors, origin, n -> consumer.accept(n.node));
  }

  private static <N> void bft(
      Function<N, Stream<N>> successors, N origin, Consumer<SearchNode<N>> consumer) {
    ArrayDeque<SearchNode<N>> fringe = new ArrayDeque<>();
    HashSet<N> visited = new HashSet<>();
    fringe.offer(new SearchNode<>(origin, null, 0));

    SearchNode<N> lastNode = null;
    while (!fringe.isEmpty()) {
      SearchNode<N> current = fringe.poll();
      if (!visited.add(current.node)) {
        continue;
      }
      consumer.accept(current);
      successors.apply(current.node).forEach(next -> {
        if (!visited.contains(next)) {
          fringe.offer(new SearchNode<>(next, current, current.cost + 1));
        }
      });
      lastNode = current;
    }
  }

  private static <N> List<N> topologicalSort(Graph<N> dag) {
    // TODO: implement.
    return null;
  }

  private static <N, E> Function<N, Iterable<Pair<N, E>>> successorFromValueGraph(
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

  private static class DefaultVisitor<N> implements Visitor<N> {
    private final HashSet<N> visited = new HashSet<>();

    @Override
    public boolean hasVisited(N n) {
      return visited.contains(n);
    }

    @Override
    public boolean visit(N n) {
      return visited.add(n);
    }
  }
}
