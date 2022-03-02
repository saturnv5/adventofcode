package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Day7 extends Day {
  public static void main(String[] args) {
    new Day7().solve();
  }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern(" |[()]|->|,").omitEmptyStrings();

  private MutableGraph<String> programs;
  private Map<String, Integer> weights;

  @Override
  protected void prepare(List<String> lines) {
    programs = GraphBuilder.directed().build();
    weights = new HashMap<>();
    lines.stream().map(INPUT_SPLITTER::splitToList).forEach(tokens -> {
      String base = tokens.get(0);
      programs.addNode(base);
      weights.put(base, Integer.parseInt(tokens.get(1)));
      tokens.stream().skip(2).forEach(n -> programs.putEdge(base, n));
    });
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    String head = programs.nodes().stream().filter(n -> programs.inDegree(n) == 0).findFirst().get();
    if (part1) return head;
    String unbalancedProgram = findUnbalanced(head);
    int incorrectWeight = memoizedTotalWeight.apply(unbalancedProgram);
    int correctWeight = memoizedTotalWeight.apply(getSibling(unbalancedProgram));
    return weights.get(unbalancedProgram) + (correctWeight - incorrectWeight);
  }

  private String findUnbalanced(String head) {
    Set<String> children = programs.successors(head);
    if (children.size() < 3) return null; // Need at least 3 children to determine an odd one out.
    HashMap<Integer, List<String>> possibleWeights = new HashMap<>();
    for (String child : children) {
      possibleWeights.computeIfAbsent(memoizedTotalWeight.apply(child), ArrayList::new).add(child);
    }
    Optional<String> defectiveProgram =
        possibleWeights.values().stream().filter(l -> l.size() == 1).map(l -> l.get(0)).findFirst();
    if (defectiveProgram.isEmpty()) return null;
    String deeperDefect = findUnbalanced(defectiveProgram.get());
    return deeperDefect == null ? defectiveProgram.get() : deeperDefect;
  }

  private String getSibling(String program) {
    String parent = Iterables.getOnlyElement(programs.predecessors(program));
    return programs.successors(parent)
        .stream()
        .filter(Predicate.not(program::equals))
        .findFirst()
        .get();
  }

  private final Function<String, Integer> memoizedTotalWeight = Memoizer.memoize(this::totalWeight);

  private int totalWeight(String program) {
    int weight = weights.get(program);
    for (String child : programs.successors(program)) {
      weight += memoizedTotalWeight.apply(child);
    }
    return weight;
  }
}
