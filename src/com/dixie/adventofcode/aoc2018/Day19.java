package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.aoc2018.common.Instruction;
import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day19 extends Day {
  public static void main(String[] args) {
    new Day19().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    int ipReg = Integer.parseInt(lines.get(0).substring(4));
    List<Instruction> program = lines.stream().skip(1).map(Instruction::parse).toList();
    int[] r = new int[6];
    int ip = 0;
    return super.part1(lines);
  }
}
