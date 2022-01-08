package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;
import java.util.stream.IntStream;

public class Day19 extends Day {
  public static void main(String[] args) {
    new Day19().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    long[] program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
    Space2D<Boolean> space = new Space2D<>();
    IntStream.range(0, 50)
        .forEach(y -> IntStream.range(0, 50)
            .forEach(x -> space.setValueAt(x, y, new Intcode(program).executeUntilEnd(x, y) == 1)));
    System.out.println(space.toPrintableImage(b -> b ? "█" : " "));
    return space.streamOccurrencesOf(true).count();
  }
}
