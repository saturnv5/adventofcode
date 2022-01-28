package com.dixie.adventofcode.lib;

import com.google.common.base.Predicates;
import com.google.common.graph.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GraphUtils {

  public static <N> Path<N> shortestPath(Graph<N> graph, N origin, N destination) {
    return shortestNonWeightedPath(n -> graph.successors(n).stream(), origin, destination);
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
    AtomicReference<SearchNode<N>> lastNode = new AtomicReference<>();
    AtomicBoolean foundEnd = new AtomicBoolean(false);
    breadthFirstTraversal(successors, origin, visitor, n -> lastNode.set(n), n -> {
      if (endCondition.test(n.getNode())) {
        foundEnd.set(true);
        return true;
      }
      return false;
    });
    if (foundEnd.get()) {
      return lastNode.get().constructPath();
    } else {
      return null;
    }
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
      if (endCondition.test(current.getNode())) {
        return current.constructPath();
      }
      if (!visitor.visit(current.getNode())) {
        continue;
      }
      for (Pair<N, Long> next : successors.apply(current.getNode())) {
        if (!visitor.hasVisited(next.first)) {
          fringe.offer(new SearchNode<>(next.first, current, current.getCost() + next.second));
        }
      }
    }
    return null; // Not found.
  }

  public static <N> Path<N> longestBfsPath(Function<N, Stream<N>> successors, N origin) {
    AtomicReference<SearchNode<N>> lastNode = new AtomicReference<>();
    breadthFirstTraversal(
        successors, origin, new DefaultVisitor<>(), n -> lastNode.set(n), Predicates.alwaysFalse());
    return lastNode.get() == null
        ? null
        : lastNode.get().constructPath();
  }

  public static <N> void breadthFirstTraversal(Graph<N> graph, N origin, Consumer<N> consumer) {
    breadthFirstTraversal(n -> graph.successors(n).stream(), origin, consumer);
  }

  public static <N> void breadthFirstTraversal(
      Function<N, Stream<N>> successors, N origin, Consumer<N> consumer) {
    breadthFirstTraversal(successors, origin,
        new DefaultVisitor<>(), n -> consumer.accept(n.getNode()), Predicates.alwaysFalse());
  }

  public static <N> void breadthFirstTraversal(
      Function<N, Stream<N>> successors, N origin,
      Visitor<N> visitor, Consumer<SearchNode<N>> consumer, Predicate<SearchNode<N>> endCondition) {
    ArrayDeque<SearchNode<N>> fringe = new ArrayDeque<>();
    fringe.offer(new SearchNode<>(origin, null, 0));

    while (!fringe.isEmpty()) {
      SearchNode<N> current = fringe.poll();
      if (!visitor.visit(current.getNode())) {
        continue;
      }
      consumer.accept(current);
      if (endCondition.test(current)) {
        return;
      }
      successors.apply(current.getNode()).forEach(next -> {
        if (!visitor.hasVisited(next)) {
          fringe.offer(new SearchNode<>(next, current, current.getCost() + 1));
        }
      });
    }
  }

  public static <N extends Comparable<N>> void topologicalTraversal(
      Graph<N> dag, Consumer<N> consumer) {
    PriorityQueue<N> fringe = new PriorityQueue<>();
    dag.nodes().stream().filter(n -> dag.inDegree(n) == 0).forEach(fringe::offer);
    DefaultVisitor<N> visitor = new DefaultVisitor<>();
    while (!fringe.isEmpty()) {
      N current = fringe.poll();
      if (!visitor.visit(current)) {
        continue;
      }
      consumer.accept(current);
      Graphs.reachableNodes(dag, current).stream()
          .filter(Predicate.not(visitor::hasVisited))
          .filter(s -> dag.predecessors(s).stream().allMatch(visitor::hasVisited))
          .forEach(fringe::offer);
    }
  }

  private static <N, E> Function<N, Iterable<Pair<N, E>>> successorFromValueGraph(
      ValueGraph<N, E> graph) {
    return n -> graph.successors(n)
        .stream()
        .map(m -> Pair.of(m, graph.edgeValue(n, m).get()))
        .toList();
  }

  public static class DefaultVisitor<N> implements Visitor<N> {
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
