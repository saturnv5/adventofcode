package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 extends Day {
  public static void main(String[] args) {
    new Day4().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    return lines.stream().filter(Day4::hasRepeats).count();
  }

  @Override
  protected Object part2(List<String> lines) {
    return lines.stream().filter(Day4::hasAnagramRepeats).count();
  }

  private static boolean hasRepeats(String passphrase) {
    HashSet<String> words = new HashSet<>();
    for (String word : passphrase.split(" ")) {
      if (!words.add(word)) return false;
    }
    return true;
  }

  private static boolean hasAnagramRepeats(String passphrase) {
    HashSet<Multiset<Integer>> anagrams = new HashSet<>();
    for (String word : passphrase.split(" ")) {
      Multiset<Integer> anagram =
          word.chars().boxed().collect(ImmutableMultiset.toImmutableMultiset());
      if (!anagrams.add(anagram)) return false;
    }
    return true;
  }
}
