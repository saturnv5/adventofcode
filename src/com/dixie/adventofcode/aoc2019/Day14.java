package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.google.common.base.Preconditions;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.HashMap;
import java.util.List;

public class Day14 extends Day {
  public static void main(String[] args) {
    new Day14().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    ValueGraph<String, Reaction> reactionTree = parseTree(lines);
    return findOreCost(reactionTree, new HashMap<>(), "FUEL", 1);
  }

  private static ValueGraph<String, Reaction> parseTree(List<String> lines) {
    MutableValueGraph<String, Reaction> tree = ValueGraphBuilder.directed().build();
    for (String line : lines) {
      String[] edge = line.split(" => ");
      String[] output = edge[1].split(" ");
      int outputIncrement = Integer.parseInt(output[0]);
      String[] inputs = edge[0].split(", ");
      for (String input : inputs) {
        String[] reactant = input.split(" ");
        int inputIncrement = Integer.parseInt(reactant[0]);
        Reaction productionFraction = new Reaction(inputIncrement, outputIncrement);
        tree.putEdgeValue(output[1], reactant[1], productionFraction);
      }
    }
    return tree;
  }

  private static long findOreCost(ValueGraph<String, Reaction> reactionTree,
      HashMap<String, Long> spareInputs, String output, long numOutputs) {
    return -1;
  }

  private static class Reaction {
    final long inputIncrement;
    final long outputIncrement;

    Reaction(long inputIncrement, long outputIncrement) {
      this.inputIncrement = inputIncrement;
      this.outputIncrement = outputIncrement;
    }
  }
}
