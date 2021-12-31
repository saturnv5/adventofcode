package com.google.adventofcode.lib;

import java.util.List;

public class Path<N> {
  private final List<N> nodes;
  private final long cost;

  Path(List<N> nodes, long cost) {
    this.nodes = nodes;
    this.cost = cost;
  }

  public List<N> getNodes() {
    return nodes;
  }

  public long getCost() {
    return cost;
  }
}
