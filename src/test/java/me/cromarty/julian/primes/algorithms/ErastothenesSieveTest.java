package me.cromarty.julian.primes.algorithms;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import me.cromarty.julian.primes.PrimesFinder;

/**
 * @author Julian Cromarty
 */
@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class ErastothenesSieveTest {

  PrimesFinder sieve = new ErastothenesSieve();

  @Test(expected = IllegalArgumentException.class)
  @Parameters({ "-2147483648", "-1", "0", "1" })
  public void testInvalidInitialNumbers(final int initial) {
    sieve.getPrimes(initial);
  }

  @Test
  @Parameters(method = "primesToTen")
  public void testPrimesToTen(final int initial, final Set<Integer> expectedPrimes) {
    assertEquals(expectedPrimes, sieve.getPrimes(initial).getPrimes());
  }

  @Test
  public void testCache() {
    final Set<Integer> firstRun = sieve.getPrimes(97).getPrimes();
    final Set<Integer> secondRun = sieve.getPrimes(97).getPrimes();
    assertEquals(firstRun, secondRun);
  }

  @Test
  public void testCachePartial() {
    final Set<Integer> firstRun = sieve.getPrimes(100).getPrimes();
    final Set<Integer> secondRun = sieve.getPrimes(200).getPrimes();
    secondRun.removeIf(i -> i > 100);
    assertEquals(firstRun, secondRun);
  }

  //@formatter:off
  private Object[] primesToTen() {
    return new Object[] {
        new Object[] { 2, set(2) },
        new Object[] { 3, set(2, 3) },
        new Object[] { 4, set(2, 3) },
        new Object[] { 5, set(2, 3, 5) },
        new Object[] { 6, set(2, 3, 5) },
        new Object[] { 7, set(2, 3, 5, 7) },
        new Object[] { 8, set(2, 3, 5, 7) },
        new Object[] { 9, set(2, 3, 5, 7) },
        new Object[] { 10, set(2, 3, 5, 7) }
    };
  }
  //@formatter:on

  private Set<Integer> set(final Integer... ints) {
    return new HashSet<>(Arrays.asList(ints));
  }
}
