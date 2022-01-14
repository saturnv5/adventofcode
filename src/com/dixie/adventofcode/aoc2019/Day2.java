package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;

public class Day2 extends Day {
  public static void main(String[] args) {
    new Day2().solve();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    int[] memory = StreamUtils.streamInts(lines.get(0), ",").toArray();
    if (part1) {
      return execute(memory, 12, 2);
    } else {
      for (int n = 0; n < 100; n++) {
        for (int v = 0; v < 100; v++) {
          if (execute(memory, n, v) == 19690720) {
            return 100 * n + v;
          }
        }
      }
    }
    return 0;
  }

  private int execute(int[] memory, int noun, int verb) {
    memory[1] = noun;
    memory[2] = verb;
    new Intcode(memory).executeUntilEnd();
    return memory[0];
  }
}
