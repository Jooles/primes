package me.julian.cromarty.primes.algorithms;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import me.cromarty.julian.primes.PrimesFinder;
import me.cromarty.julian.primes.algorithms.ErastothenesSieve;

/**
 * @author Julian Cromarty
 */
@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class SieveTest {

  PrimesFinder sieve = new ErastothenesSieve();

  @Test(expected = IllegalArgumentException.class)
  @Parameters({ "-2147483648", "-1", "0" })
  public void testInvalidInitialNumbers(final int initial) {
    sieve.getPrimes(initial);
  }

  @Test
  @Parameters(method = "primesToTen")
  public void testPrimesToTen(final int initial, final Set<Integer> expectedPrimes) {
    assertEquals(expectedPrimes, sieve.getPrimes(initial));
  }

  private Object[] primesToTen() {
    return new Object[] { new Object[] { 1, new HashSet<>(Arrays.asList(1)) },
        new Object[] { 2, new HashSet<>(Arrays.asList(1, 2)) },
        new Object[] { 3, new HashSet<>(Arrays.asList(1, 2, 3)) },
        new Object[] { 4, new HashSet<>(Arrays.asList(1, 2, 3)) },
        new Object[] { 5, new HashSet<>(Arrays.asList(1, 2, 3, 5)) },
        new Object[] { 6, new HashSet<>(Arrays.asList(1, 2, 3, 5)) },
        new Object[] { 7, new HashSet<>(Arrays.asList(1, 2, 3, 5, 7)) },
        new Object[] { 8, new HashSet<>(Arrays.asList(1, 2, 3, 5, 7)) },
        new Object[] { 9, new HashSet<>(Arrays.asList(1, 2, 3, 5, 7)) },
        new Object[] { 10, new HashSet<>(Arrays.asList(1, 2, 3, 5, 7)) } };
  }

}
