package com.dixie.adventofcode.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchNode<N> implements Comparable<SearchNode<N>> {
  private final N node;
  private final SearchNode<N> predeccessor;
  private final long cost;

  public SearchNode(N node, SearchNode<N> predeccessor, long cost) {
    this.node = node;
    this.predeccessor = predeccessor;
    this.cost = cost;
  }

  public N getNode() {
    return node;
  }

  public SearchNode<N> getPredeccessor() {
    return predeccessor;
  }

  public long getCost() {
    return cost;
  }

  public Path<N> constructPath() {
    return new Path<>(constructPathList(), cost);
  }

  private List<N> constructPathList() {
    ArrayList<N> path = new ArrayList<>();
    SearchNode<N> n = this;
    while (n != null) {
      path.add(n.node);
      n = n.predeccessor;
    }
    Collections.reverse(path);
    return path;
  }

  @Override
  public int compareTo(SearchNode<N> o) {
    return Long.compare(cost, o.cost);
  }
}