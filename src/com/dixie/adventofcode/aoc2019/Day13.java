package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;

public class Day13 extends Day {
  public static void main(String[] args) {
    new Day13().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    Intcode ic = new Intcode(StreamUtils.streamLongs(lines.get(0), ",").toArray());
    int numBlocks = 0;
    while (!ic.hasHalted()) {
      ic.executeUntilOutput(0);
      ic.executeUntilOutput(0);
      Long tile = ic.executeUntilOutput(0);
      if (tile != null && tile == 2) {
        numBlocks++;
      }
    }
    return numBlocks;
  }
}
