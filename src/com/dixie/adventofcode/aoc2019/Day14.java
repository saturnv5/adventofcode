package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.MathUtils;
import com.dixie.adventofcode.lib.Pair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.HashMap;
import java.util.List;

public class Day14 extends Day {
  public static void main(String[] args) {
    new Day14().solve();
  }

  private ValueGraph<String, Reaction> reactionTree;

  @Override
  protected void prepare(List<String> lines) {
    reactionTree = parseTree(lines);
  }

  @Override
  protected Object part1(List<String> lines) {
    return findOreCost(new HashMap<>(), "FUEL", 1);
  }

  @Override
  protected Object part2(List<String> lines) {
    long numOres = 1_000_000_000_000L;
    long maxFuel = 1;
    long maxOreCost = findOreCost(new HashMap<>(), "FUEL", maxFuel);
    while (maxOreCost < numOres) {
      long diff = (long) Math.floor(numOres / (maxOreCost / (double) maxFuel)) - maxFuel;
      if (diff <= 0) {
        break;
      }
      maxFuel += diff;
      maxOreCost = findOreCost(new HashMap<>(), "FUEL", maxFuel);
    }
    return maxFuel;
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

  private long findOreCost(HashMap<String, Long> spareInputs, String output, long numOutputs) {
    if (output.equals("ORE")) {
      return numOutputs;
    }
    long oreCost = 0;
    for (String input : reactionTree.successors(output)) {
      Reaction reaction = reactionTree.edgeValue(output, input).get();
      Pair<Long, Long> amounts = reaction.amountsForOutput(numOutputs);
      long spares = spareInputs.getOrDefault(input, 0L);
      if (amounts.first <= spares) {
        spareInputs.put(input, spares - amounts.first);
      } else {
        spareInputs.put(input, 0L);
        oreCost += findOreCost(spareInputs, input, amounts.first - spares);
      }
      if (amounts.second > numOutputs) {
        spareInputs.put(output, amounts.second - numOutputs);
      }
    }
    return oreCost;
  }

  private record Reaction(long inputIncrement, long outputIncrement) {

    Pair<Long, Long> amountsForOutput(long outputs) {
      long factor = MathUtils.ceilDiv(outputs, outputIncrement);
      return Pair.of(inputIncrement * factor, outputIncrement * factor);
    }
  }
}
