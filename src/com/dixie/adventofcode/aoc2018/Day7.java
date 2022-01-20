package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.Pair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.List;

public class Day7 extends Day {
  public static void main(String[] args) {
    new Day7().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    MutableGraph<Character> dag = GraphBuilder.directed().build();
    lines.stream().map(Day7::parseEdge).forEach(edge -> dag.putEdge(edge.first, edge.second));
    StringBuilder sb = new StringBuilder();
    GraphUtils.topologicalTraversal(dag, sb::append);
    return sb;
  }

  private static Pair<Character, Character> parseEdge(String line) {
    return Pair.of(line.charAt(5), line.charAt(36));
  }
}
