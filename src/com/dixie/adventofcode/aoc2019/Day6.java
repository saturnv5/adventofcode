package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.google.common.collect.Iterables;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.List;

public class Day6 extends Day {
  public static void main(String[] args) {
    new Day6().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    return depthSum(parseTree(lines, true), "COM", 1);
  }

  @Override
  protected long part2(List<String> lines) {
    Graph<String> tree = parseTree(lines, false);
    return GraphUtils.shortestPath(tree, parent(tree, "YOU"), parent(tree, "SAN")).getCost();
  }

  private static Graph<String> parseTree(List<String> lines, boolean directed) {
    MutableGraph<String> tree = directed ? GraphBuilder.directed()
            .build() : GraphBuilder.undirected().build();
    for (String line : lines) {
      String[] nodes = line.split("\\)");
      tree.putEdge(nodes[0], nodes[1]);
    }
    return tree;
  }

  private static int depthSum(Graph<String> tree, String node, int depth) {
    int sum = 0;
    for (String n : tree.successors(node)) {
      sum += depthSum(tree, n, depth + 1) + depth;
    }
    return sum;
  }

  private static String parent(Graph<String> tree, String node) {
    return Iterables.getOnlyElement(tree.predecessors(node));
  }
}
