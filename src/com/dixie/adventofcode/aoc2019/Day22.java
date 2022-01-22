package com.dixie.adventofcode.aoc2019;

import static com.dixie.adventofcode.lib.MathUtils.bigInt;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class Day22 extends Day {
  public static void main(String[] args) {
    new Day22().solve();
  }

  private static final int TOTAL_CARDS_1 = 10007;
  private static final long TOTAL_CARDS_2 = 119315717514047L;
  private static final BigInteger TOTAL_CARDS_BIG_INT = bigInt(TOTAL_CARDS_2);
  private static final BigInteger TOTAL_SHUFFLES = bigInt(101741582076661L);

  @Override
  protected Object part1(List<String> lines) {
    IntUnaryOperator shuffle = lines.stream()
        .map(Day22::parseFunction)
        .reduce(IntUnaryOperator.identity(), IntUnaryOperator::andThen);
    return shuffle.applyAsInt(2019);
  }

  @Override
  protected Object part2(List<String> lines) {
    Collections.reverse(lines);
    Pair<BigInteger, BigInteger> inverseShufflePolynomial = lines.stream()
        .map(Day22::parseToPolynomial)
        .reduce(Pair.of(BigInteger.ONE, BigInteger.ZERO), Day22::compose);
    BigInteger a = inverseShufflePolynomial.first, b = inverseShufflePolynomial.second;
    // k = no. shuffles
    // m = (a^k - 1) * inv(a-1) mod N
    BigInteger atoK = a.modPow(TOTAL_SHUFFLES, TOTAL_CARDS_BIG_INT);
    BigInteger m = atoK.subtract(BigInteger.ONE)
        .multiply(a.subtract(BigInteger.ONE).modInverse(TOTAL_CARDS_BIG_INT))
        .mod(TOTAL_CARDS_BIG_INT);
    // x -> x*a^k + m*b mod N
    return bigInt(2020).multiply(atoK)
        .add(m.multiply(b))
        .mod(TOTAL_CARDS_BIG_INT)
        .longValue();
  }

  private static IntUnaryOperator parseFunction(String line) {
    if (line.equals("deal into new stack")) {
      return i -> TOTAL_CARDS_1 - i - 1;
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return i -> Math.floorMod(i - cut, TOTAL_CARDS_1);
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return i -> Math.floorMod(i * inc, TOTAL_CARDS_1);
    }
    return IntUnaryOperator.identity();
  }

  /** Returns (a, b) representing polynomials of the form: a*x + b mod N */
  private static Pair<BigInteger, BigInteger> parseToPolynomial(String line) {
    if (line.equals("deal into new stack")) {
      return Pair.of(BigInteger.ONE.negate(), bigInt(TOTAL_CARDS_2 - 1));
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return Pair.of(BigInteger.ONE, bigInt(cut));
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return Pair.of(bigInt(inc).modInverse(TOTAL_CARDS_BIG_INT), BigInteger.ZERO);
    }
    return Pair.of(BigInteger.ONE, BigInteger.ZERO);
  }

  /** Given f(x) and g(x) polynomials, return g(f(x)). */
  private static Pair<BigInteger, BigInteger> compose(Pair<BigInteger, BigInteger> f,
      Pair<BigInteger, BigInteger> g) {
    return Pair.of(f.first.multiply(g.first).mod(TOTAL_CARDS_BIG_INT),
        g.first.multiply(f.second).add(g.second).mod(TOTAL_CARDS_BIG_INT));
  }
}
