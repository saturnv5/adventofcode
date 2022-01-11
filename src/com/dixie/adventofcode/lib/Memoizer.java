package com.dixie.adventofcode.lib;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Memoizer<T, R> implements Function<T, R> {
  private final HashMap<T, R> cache = new HashMap<>();
  private final Function<T, R> loader;

  public static <T, R> Function<T, R> memoize(Function<T, R> function) {
    return new Memoizer<>(function);
  }

  public static <T, U, R> BiFunction<T, U, R> memoize(BiFunction<T, U, R> function) {
    Memoizer<Pair<T, U>, R> memoizer = new Memoizer<>(in -> function.apply(in.first, in.second));
    return Pair.<T, U>toPair().andThen(memoizer);
  }

  private Memoizer(Function<T, R> loader) {
    this.loader = loader;
  }

  @Override
  public R apply(T t) {
    R val = cache.get(t);
    if (val == null) {
      val = loader.apply(t);
      cache.put(t, val);
    }
    return val;
  }
}
