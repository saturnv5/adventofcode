package com.dixie.adventofcode.aoc2017.common;

import java.util.stream.IntStream;

public class KnotHash {
  public static int[] computeHash(String input) {
    int[] list = IntStream.range(0, 256).toArray();
    int[] hashLengths = IntStream.range(0, 64)
        .flatMap(i -> IntStream.concat(input.chars(), IntStream.of(17, 31, 73, 47, 23)))
        .toArray();
    performHash(list, hashLengths);
    int[] denseHash = new int[16];
    for (int i = 0; i < denseHash.length; i++) {
      int hash = list[i * 16];
      for (int j = 1; j < 16; j++) {
        hash ^= list[i * 16 + j];
      }
      denseHash[i] = hash;
    }
    return denseHash;
  }

  public static int performHash(int[] list, int[] hashLengths) {
    int pos = 0, skip = 0;
    for (int length : hashLengths) {
      reverseRange(list, pos, length);
      pos = (pos + length + skip++) % list.length;
    }
    return list[0] * list[1];
  }

  private static void reverseRange(int[] list, int from, int length) {
    int[] subList = new int[length];
    for (int i = 0, j = from; i < subList.length; i++, j = (j + 1) % list.length) {
      subList[i] = list[j];
    }
    for (int i = subList.length - 1, j = from; i >= 0; i--, j = (j + 1) % list.length) {
      list[j] = subList[i];
    }
  }
}
