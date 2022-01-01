package com.dixie.adventofcode.lib;

import java.util.Objects;
import java.util.function.BiFunction;

public class Pair<A, B> {
  public final A first;
  public final B second;

  public static <A, B> Pair<A, B> of(A first, B second) {
    return new Pair<>(first, second);
  }

  public static <A, B> BiFunction<A, B, Pair<A, B>> toPair() {
    return Pair::of;
  }

  private Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
