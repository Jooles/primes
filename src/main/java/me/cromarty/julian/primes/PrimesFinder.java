package me.cromarty.julian.primes;

import java.util.Set;

/**
 * @author Julian Cromarty
 */
public interface PrimesFinder {

  /**
   * Find all the prime numbers up to and including the initial number
   *
   * @param initial
   *          The number used to bound the search
   * @return A {@link Set} of the prime numbers found
   * @throws IllegalArgumentException
   *           if the initial parameter is less than 1
   */
  public Set<Integer> getPrimes(final int initial);
}
