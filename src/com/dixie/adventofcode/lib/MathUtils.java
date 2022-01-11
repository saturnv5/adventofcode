package com.dixie.adventofcode.lib;

import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.util.Arrays;

public class MathUtils {
  public static long lcm(long a, long b) {
    return (a * b) / LongMath.gcd(a, b);
  }

  public static long lcm(long... numbers) {
    return Arrays.stream(numbers).reduce(1, MathUtils::lcm);
  }

  public static long ceilDiv(long dividend, long divisor) {
    return (dividend + divisor - 1) / divisor;
  }

  public static long modInverse(long num, long modulus) {
    return BigInteger.valueOf(num).modInverse(BigInteger.valueOf(modulus)).longValue();
  }
}
