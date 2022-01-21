package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.Pair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day7 extends Day {
  public static void main(String[] args) {
    new Day7().solve();
  }

  private static final int TOTAL_WORKERS = 5;

  private MutableGraph<Character> steps;
  private Set<Character> remainingSteps;
  private Set<Character> inProgressSteps;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    steps = GraphBuilder.directed().build();
    lines.stream().map(Day7::parseEdge).forEach(edge -> steps.putEdge(edge.first, edge.second));
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    StringBuilder sb = new StringBuilder();
    GraphUtils.topologicalTraversal(steps, sb::append);
    return sb;
  }

  @Override
  protected Object part2(List<String> lines) {
    remainingSteps = new HashSet<>(steps.nodes());
    inProgressSteps = new HashSet<>();
    ArrayDeque<Worker> workers = new ArrayDeque<>();
    int totalTime = 0;
    while (!remainingSteps.isEmpty()) {
      fillWorkers(workers);
      totalTime += waitForNextWorker(workers);
    }
    return totalTime;
  }

  private void fillWorkers(ArrayDeque<Worker> workers) {
    remainingSteps.stream()
        .filter(Predicate.not(inProgressSteps::contains))
        .filter(s -> steps.predecessors(s).stream().noneMatch(remainingSteps::contains))
        .sorted(Comparator.<Character>comparingLong(
                step -> Graphs.reachableNodes(steps, step).stream()
                    .filter(remainingSteps::contains)
                    .count())
            .reversed())
        .takeWhile(s -> workers.size() < TOTAL_WORKERS)
        .forEachOrdered(s -> {
          workers.offerLast(new Worker(s));
          inProgressSteps.add(s);
        });
  }

  private int waitForNextWorker(ArrayDeque<Worker> workers) {
    if (workers.isEmpty()) {
      return 0;
    }
    int timeToAdvance = workers.peekFirst().timeRemaining;
    workers.forEach(w -> w.timeRemaining -= timeToAdvance);
    while (!workers.isEmpty() && workers.peekFirst().isFinished()) {
      char step = workers.pollFirst().step;
      inProgressSteps.remove(step);
      remainingSteps.remove(step);
    }
    return timeToAdvance;
  }

  private static Pair<Character, Character> parseEdge(String line) {
    return Pair.of(line.charAt(5), line.charAt(36));
  }

  private static class Worker {
    final char step;
    int timeRemaining;

    Worker(char step) {
      this.step = step;
      timeRemaining = 61 + step - 'A';
    }

    boolean isFinished() {
      return timeRemaining <= 0;
    }
  }
}
