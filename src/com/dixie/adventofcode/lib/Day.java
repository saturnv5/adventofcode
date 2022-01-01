package com.dixie.adventofcode.lib;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public abstract class Day {

  public void solve() {
    System.out.println("Input:");
    Scanner scan = new Scanner(System.in);
    List<String> lines = StreamUtils.streamLines(scan)
            .takeWhile(line -> !line.equals("end"))
            .collect(Collectors.toList());
    System.out.println("Part 1: " + solve(lines, true));
    System.out.println("Part 2: " + solve(lines, false));
  }

  protected long solve(List<String> lines, boolean part1) {
    return part1 ? part1(lines) : part2(lines);
  }

  protected long part1(List<String> lines) {
    return 0;
  }

  protected long part2(List<String> lines) {
    return 0;
  }
}
