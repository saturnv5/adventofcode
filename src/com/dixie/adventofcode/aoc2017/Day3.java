package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Day3 extends Day {
  public static void main(String[] args) {
    new Day3().solve();
  }

  private Point[] spiral;

  @Override
  protected void prepare(List<String> lines) {
    int target = Integer.parseInt(lines.get(0));
    spiral = new Point[target + 1];
    Point p = new Point(0, 0);
    spiral[1] = p;
    Direction dir = Direction.RIGHT;
    int len = 1;
    int line = 0;
    int num = 2;
    while (num <= target) {
      for (int l = 0; l < len && num <= target; l++) {
        p = dir.apply(p);
        spiral[num++] = p;
      }
      dir = dir.turnLeft();
      line++;
      if (line % 2 == 0) len++;
    }
  }

  @Override
  protected Object part1(List<String> lines) {
    Point last = spiral[spiral.length - 1];
    return Math.abs(last.x) + Math.abs(last.y);
  }

  @Override
  protected Object part2(List<String> lines) {
    Space2D<Integer> sumSpiral = new Space2D<>();
    sumSpiral.setValueAt(new Point(0, 0), 1);
    for (int i = 2; i < spiral.length; i++) {
      Point p = spiral[i];
      int sum = Arrays.stream(Direction.values())
          .map(dir -> dir.apply(p))
          .mapToInt(c -> sumSpiral.getValueAt(c, 0))
          .sum();
      sumSpiral.setValueAt(p, sum);
      if (sum >= spiral.length) {
        return sum;
      }
    }
    return 0;
  }
}
