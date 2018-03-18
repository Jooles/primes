package me.cromarty.julian.primes.algorithms;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.cromarty.julian.primes.PrimesFinder;

/**
 * Naive implementation of a Sieve of Erastothenes for finding primes
 *
 * @author Julian Cromarty
 */
public class ErastothenesSieve implements PrimesFinder {

  @Override
  public Set<Integer> getPrimes(final int initial) {
    if (initial <= 0) {
      throw new IllegalArgumentException("Initial number must be greater than 0");
    }
    final Set<Integer> primes = generateIntegers(initial);
    for (int i = 2; i <= initial; i++) {
      eliminateMultiples(primes, i);
    }
    return primes;
  }

  private void eliminateMultiples(final Set<Integer> ints, final int multiple) {
    for (final Integer i : ints) {
      if (((i % multiple) == 0) && (i > multiple)) {
        ints.remove(i);
      }
    }
  }

  private Set<Integer> generateIntegers(final int max) {
    final Set<Integer> ints = ConcurrentHashMap.newKeySet();
    for (int i = 2; i <= max; i++) {
      ints.add(i);
    }
    return ints;
  }

}
