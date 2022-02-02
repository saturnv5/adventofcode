package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Splitter;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class Day17 extends Day {
  public static void main(String[] args) {
    new Day17().solve();
  }

  private enum Cell { SPRING, CLAY, WET_SAND, WATER }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern(".=|, |\\.\\.").omitEmptyStrings();
  private static final Point SPRING = new Point(500, 0);

  private final Space2D<Cell> ground = new Space2D<>();

  @Override
  protected void prepare(List<String> lines) {
    ground.setValueAt(SPRING, Cell.SPRING);
    lines.forEach(this::parseLine);
  }

  @Override
  protected Object part1(List<String> lines) {
    return ground.toPrintableImage(Day17::cellToString);
  }

  private void parseLine(String line) {
    int[] tokens = INPUT_SPLITTER.splitToStream(line).mapToInt(Integer::parseInt).toArray();
    if (line.charAt(0) == 'x') {
      int x = tokens[0];
      IntStream.rangeClosed(tokens[1], tokens[2]).forEach(y -> ground.setValueAt(x, y, Cell.CLAY));
    } else {
      int y = tokens[0];
      IntStream.rangeClosed(tokens[1], tokens[2]).forEach(x -> ground.setValueAt(x, y, Cell.CLAY));
    }
  }

  private static String cellToString(Cell cell) {
    if (cell == null) return " ";
    return switch (cell) {
      case SPRING -> "+";
      case CLAY -> "▓";
      case WET_SAND -> ".";
      case WATER -> "~";
    };
  }
}
