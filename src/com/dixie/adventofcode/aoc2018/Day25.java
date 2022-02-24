package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day25 extends Day {
  public static void main(String[] args) {
    new Day25().solve();
  }

  private static final int MAX_DIST = 3;

  private MutableGraph<Spacetime> constellations;

  @Override
  protected void prepare(List<String> lines) {
    constellations = GraphBuilder.undirected().build();
    List<Spacetime> points = new ArrayList<>();
    lines.stream().map(Spacetime::parse).forEach(p -> {
      constellations.addNode(p);
      points.stream()
          .filter(n -> n.distTo(p) <= MAX_DIST)
          .forEach(n -> constellations.putEdge(n, p));
      points.add(p);
    });
  }

  @Override
  protected Object part1(List<String> lines) {
    HashSet<Spacetime> points = new HashSet<>(constellations.nodes());
    int disjointSets = 0;
    while (!points.isEmpty()) {
      Spacetime head = points.stream().findAny().get();
      GraphUtils.breadthFirstTraversal(constellations, head, n -> points.remove(n.getNode()));
      disjointSets++;
    }
    return disjointSets;
  }

  @Override
  protected Object part2(List<String> lines) {
    return "Happy holidays!";
  }

  private record Spacetime(int w, int x, int y, int z) {
    static Spacetime parse(String line) {
      int[] vals = StreamUtils.streamInts(line, ",").toArray();
      return new Spacetime(vals[0], vals[1], vals[2], vals[3]);
    }

    int distTo(Spacetime other) {
      return Math.abs(other.w - w) + Math.abs(other.x - x) + Math.abs(other.y - y) +
          Math.abs(other.z - z);
    }
  }
}
