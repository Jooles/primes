package me.cromarty.julian.primes;

/**
 * @author Julian Cromarty
 */
public interface PrimesFinder {

  /**
   * Find all the prime numbers up to and including the initial number
   *
   * @param initial
   *          The number used to bound the search
   * @return JSON data object with the prime numbers found
   * @throws IllegalArgumentException
   *           if the initial parameter is less than 1
   */
  public PrimesResponse getPrimes(final int initial);
}
