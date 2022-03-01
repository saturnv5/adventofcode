package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.google.common.base.Splitter;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.List;

public class Day7 extends Day {
  public static void main(String[] args) {
    new Day7().solve();
  }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern(" |[()]|->|,").omitEmptyStrings();

  private MutableGraph<String> programs;

  @Override
  protected void prepare(List<String> lines) {
    programs = GraphBuilder.directed().build();
    lines.stream().map(INPUT_SPLITTER::splitToList).forEach(tokens -> {
      String base = tokens.get(0);
      programs.addNode(base);
      tokens.stream().skip(2).forEach(n -> programs.putEdge(base, n));
    });
  }

  @Override
  protected Object part1(List<String> lines) {
    return programs.nodes().stream().filter(n -> programs.inDegree(n) == 0).findFirst().get();
  }
}
