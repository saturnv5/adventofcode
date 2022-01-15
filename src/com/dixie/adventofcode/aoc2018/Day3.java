package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Splitter;

import java.awt.*;
import java.util.List;
import java.util.PrimitiveIterator;

public class Day3 extends Day {
  public static void main(String[] args) {
    new Day3().solve();
  }

  private static final Splitter SPLITTER =
      Splitter.onPattern("#| |@|,|:|x").trimResults().omitEmptyStrings();

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    List<Rectangle> claims = lines.stream().map(Day3::parseClaim).toList();
    Space2D<Integer> fabric = processClaims(claims);
    if (part1) {
      return fabric.streamAllPoints().mapToInt(fabric::getValueAt).filter(i -> i > 1).count();
    } else {
      for (int i = 0; i < claims.size(); i++) {
        if (Space2D.streamAllPointsInBounds(claims.get(i))
            .allMatch(p -> fabric.getValueAt(p, 0) == 1)) {
          return i + 1;
        }
      }
    }
    return null;
  }

  private static Space2D<Integer> processClaims(List<Rectangle> claims) {
    Space2D<Integer> fabric = new Space2D<>();
    claims.stream()
        .flatMap(Space2D::streamAllPointsInBounds)
        .forEach(p -> fabric.setValueAt(p, fabric.getValueAt(p, 0) + 1));
    return fabric;
  }

  private static Rectangle parseClaim(String claim) {
    PrimitiveIterator.OfInt tokens =
        SPLITTER.splitToStream(claim).mapToInt(Integer::parseInt).iterator();
    tokens.next();
    return new Rectangle(tokens.nextInt(), tokens.nextInt(), tokens.nextInt(), tokens.nextInt());
  }
}
