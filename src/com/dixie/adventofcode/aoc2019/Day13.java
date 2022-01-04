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

  @Override
  protected long part2(List<String> lines) {
    long[] program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
    program[0] = 2;
    Intcode ic = new Intcode(program);
    long score = -1;
    int input = 0;
    long ballPos = -1;
    long paddlePos = -1;
    while (!ic.hasHalted()) {
      Long x = ic.executeUntilOutput(input);
      Long y = ic.executeUntilOutput(input);
      Long tile = ic.executeUntilOutput(input);
      if (tile != null) {
        if (x == -1 && y == 0) {
          score = tile;
        } else if (tile == 3) {
          paddlePos = x;
        } else if (tile == 4) {
          ballPos = x;
        }
      }
      if (ballPos >= 0 && paddlePos >= 0) {
        input = Long.signum(ballPos - paddlePos);
      }
    }
    return score;
  }
}
