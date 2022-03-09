package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.aoc2017.common.KnotHash;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.Space2D;

import java.util.List;

public class Day14 extends Day {
  public static void main(String[] args) {
    new Day14().solve();
  }

  private Space2D<Boolean> disk;

  @Override
  protected void prepare(List<String> lines) {
    disk = new Space2D<>(128, 128);
    String input = lines.get(0);
    for (int y = 0; y < 128; y++) {
      String hashIn = String.format("%s-%d", input, y);
      int[] knotHash = KnotHash.computeHash(hashIn);
      for (int h = 0; h < knotHash.length; h++) {
        for (int b = 0; b < 8; b++) {
          if ((knotHash[h] & 1 << (7 - b)) != 0) {
            disk.setValueAt(h * 8 + b, y, true);
          }
        }
      }
    }
  }

  @Override
  protected Object part1(List<String> lines) {
    return disk.streamAllPoints().count();
  }

  @Override
  protected Object part2(List<String> lines) {
    return GraphUtils.findConnectedComponents(
            p -> Direction.CARDINALS.stream()
                .map(dir -> dir.apply(p))
                .filter(n -> disk.getValueAt(n, false)),
            disk.streamAllPoints().toList())
        .size();
  }
}
