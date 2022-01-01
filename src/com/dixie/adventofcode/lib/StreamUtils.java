package com.dixie.adventofcode.lib;

import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamUtils {
  public static Stream<String> streamLines(Scanner scan) {
    Iterator<String> itr = new Iterator<>() {
      @Override
      public boolean hasNext() {
        return scan.hasNextLine();
      }

      @Override
      public String next() {
        return scan.nextLine();
      }
    };
    return Streams.stream(itr);
  }

  public static IntStream streamInts(String line) {
    return streamInts(line, " ");
  }

  public static IntStream streamInts(String line, String delimiter) {
    return Arrays.stream(line.split(delimiter)).mapToInt(Integer::parseInt);
  }

  public static LongStream streamLongs(String line) {
    return streamLongs(line, " ");
  }

  public static LongStream streamLongs(String line, String delimiter) {
    return Arrays.stream(line.split(delimiter)).mapToLong(Long::parseLong);
  }
}
