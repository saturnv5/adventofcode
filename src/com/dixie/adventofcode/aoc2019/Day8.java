package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Day8 extends Day {
  public static void main(String[] args) {
    new Day8().solve();
  }

  private List<List<Integer>> layers;

  @Override
  protected void prepare(List<String> lines) {
    layers = parseLayers(lines.get(0), 25 * 6);
  }

  @Override
  protected Object part1(List<String> lines) {
    List<Integer> fewestZeros = layers.stream()
            .min(Comparator.comparing(Memoizer.memoize(l -> countOccurrences(l, 0))))
            .get();
    return countOccurrences(fewestZeros, 1) * countOccurrences(fewestZeros, 2);
  }

  @Override
  protected Object part2(List<String> lines) {
    System.out.println(printableImage(drawImage(), 25));
    return 0;
  }

  private static List<List<Integer>> parseLayers(String image, int layerSize) {
    return Lists.partition(image
            .chars()
            .map(c -> c - '0')
            .boxed()
            .toList(), layerSize);
  }

  private static int countOccurrences(List<Integer> layer, int val) {
    return (int) layer.stream().filter(i -> i == val).count();
  }

  private int[] drawImage() {
    int[] image = new int[layers.get(0).size()];
    IntStream.range(0, image.length).forEach(i ->
            image[i] = layers.stream()
                    .mapToInt(l -> l.get(i))
                    .filter(p -> p != 2)
                    .findFirst()
                    .getAsInt()
    );
    return image;
  }

  private static String printableImage(int[] image, int width) {
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, image.length).forEach(i -> {
      if (i % width == 0) {
        sb.append('\n');
      }
      sb.append(image[i] == 1 ? "██" : "  ");
    });
    return sb.toString();
  }
}
