package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 extends Day {
  public static void main(String[] args) {
    new Day8().solve();
  }

  private Node topLevelNode;

  @Override
  protected void prepare(List<String> lines) {
    topLevelNode = parse(StreamUtils.streamInts(lines.get(0)).iterator());
  }

  @Override
  protected Object part1(List<String> lines) {
    return totalMetadata(topLevelNode);
  }

  @Override
  protected Object part2(List<String> lines) {
    return memoizedNodeValue.apply(topLevelNode);
  }

  private static int totalMetadata(Node node) {
    int total = node.children.stream().mapToInt(Day8::totalMetadata).sum();
    total += Arrays.stream(node.metadata).sum();
    return total;
  }

  private final Function<Node, Integer> memoizedNodeValue = Memoizer.memoize(this::nodeValue);

  private int nodeValue(Node node) {
    if (node.children.isEmpty()) {
      return Arrays.stream(node.metadata).sum();
    }
    return Arrays.stream(node.metadata)
        .map(i -> i - 1)
        .filter(i -> i >= 0 && i < node.children.size())
        .mapToObj(node.children::get)
        .mapToInt(memoizedNodeValue::apply)
        .sum();
  }

  private static Node parse(PrimitiveIterator.OfInt itr) {
    int children = itr.nextInt(), metas = itr.nextInt();
    return new Node(Stream.generate(() -> parse(itr)).limit(children).toList(),
        IntStream.generate(itr::nextInt).limit(metas).toArray());
  }

  private record Node(List<Node> children, int[] metadata) { }
}
