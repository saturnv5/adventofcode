package com.dixie.adventofcode.lib;

public interface Visitor<N> {
  boolean hasVisited(N n);
  boolean visit(N n);
}
