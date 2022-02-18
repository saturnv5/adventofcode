package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.aoc2018.common.Instruction;
import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day19 extends Day {
  public static void main(String[] args) {
    new Day19().solve();
  }

  private int ipReg;
  private List<Instruction> program;

  @Override
  protected void prepare(List<String> lines) {
    ipReg = Integer.parseInt(lines.get(0).substring(4));
    program = lines.stream().skip(1).map(Instruction::parse).toList();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    return sumFactorsOf(getTargetNumber(part1));
  }

  private int getTargetNumber(boolean part1) {
    int[] r = new int[6];
    if (!part1) r[0] = 1;
    while (r[ipReg] >= 0 && r[ipReg] < program.size()) {
      program.get(r[ipReg]).execute(r);
      if (r[ipReg] == 0 && r[4] != 0) {
        return r[4];
      }
      r[ipReg]++;
    }
    return 0;
  }

  private int sumFactorsOf(int num) {
    int sum = 0;
    for (int i = 1; i <= num; i++) {
      if (num % i == 0) {
        sum += i;
      }
    }
    return sum;
  }
}
