package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.google.common.base.Splitter;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class Day12 extends Day {
  public static void main(String[] args) {
    new Day12().solve();
  }

  private static final Splitter INPUT_SPLITTER = Splitter.onPattern(" |,|<->").omitEmptyStrings();

  private MutableGraph<Integer> pipes;

  @Override
  protected void prepare(List<String> lines) {
    pipes = GraphBuilder.undirected().allowsSelfLoops(true).build();
    lines.stream()
        .map(l -> INPUT_SPLITTER.splitToStream(l).mapToInt(Integer::parseInt).toArray())
        .forEach(tokens -> {
          Arrays.stream(tokens).skip(1).forEach(n -> pipes.putEdge(tokens[0], n));
        });
  }

  @Override
  protected Object part1(List<String> lines) {
    LongAdder numConnected = new LongAdder();
    GraphUtils.breadthFirstTraversal(pipes, 0, n -> numConnected.increment());
    return numConnected.longValue();
  }

  @Override
  protected Object part2(List<String> lines) {
    HashSet<Integer> remainingNodes = new HashSet<>(pipes.nodes());
    int numComponents = 0;
    while (!remainingNodes.isEmpty()) {
      int from = remainingNodes.stream().findAny().get();
      GraphUtils.breadthFirstTraversal(pipes, from, n -> remainingNodes.remove(n.getNode()));
      numComponents++;
    }
    return numComponents;
  }
}
