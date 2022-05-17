package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 extends Day {
  public static void main(String[] args) {
    new Day16().solve();
  }

  private Function<List<Character>, List<Character>> dance;

  @Override
  protected void prepare(List<String> lines) {
    dance = parseMoves(lines.get(0));
  }

  @Override
  protected Object part1(List<String> lines) {
    List<Character> programs = IntStream.rangeClosed('a', 'p')
        .mapToObj(c -> (char) c)
        .collect(Collectors.toCollection(ArrayList::new));
    return executeDance(programs);
  }

  @Override
  protected Object part2(List<String> lines) {
    List<Character> programs = IntStream.rangeClosed('a', 'p')
        .mapToObj(c -> (char) c)
        .collect(Collectors.toCollection(ArrayList::new));
    String target = "abcdefghijklmnop";
    List<String> danceCycle = new ArrayList<>();
    while (danceCycle.isEmpty() || !Iterables.getLast(danceCycle).equals(target))
      danceCycle.add(executeDance(programs));
    int targetReps = (int) 1E9 % danceCycle.size();
    return danceCycle.get(targetReps - 1);
  }

  private String executeDance(List<Character> programs) {
    programs = dance.apply(programs);
    return programs.stream().map(String::valueOf).collect(Collectors.joining());
  }

  private static Function<List<Character>, List<Character>> parseMoves(String moves) {
    List<Function<List<Character>, List<Character>>> functions = Splitter.on(',')
        .splitToStream(moves)
        .map(Day16::parseMove)
        .toList();
    return programs -> {
      functions.forEach(f -> f.apply(programs));
      return programs;
    };
  }

  private static Function<List<Character>, List<Character>> parseMove(String move) {
    return switch (move.charAt(0)) {
      case 's' -> programs -> {
        int dist = Integer.parseInt(move.substring(1));
        Collections.rotate(programs, dist);
        return programs;
      };
      case 'x' -> programs -> {
        String[] positions = move.substring(1).split("/");
        int posA = Integer.parseInt(positions[0]), posB = Integer.parseInt(positions[1]);
        Collections.swap(programs, posA, posB);
        return programs;
      };
      case 'p' -> programs -> {
        int posA = programs.indexOf(move.charAt(1)), posB = programs.indexOf(move.charAt(3));
        Collections.swap(programs, posA, posB);
        return programs;
      };
      default -> throw new IllegalArgumentException("Unknown move.");
    };
  }
}
