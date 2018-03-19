package me.cromarty.julian.primes.algorithms;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.cromarty.julian.primes.PrimesFinder;
import me.cromarty.julian.primes.PrimesResponse;

/**
 * Naive implementation of a Sieve of Erastothenes for finding primes
 *
 * @author Julian Cromarty
 */
public class ErastothenesSieve implements PrimesFinder {

  private final Set<Integer> primesCache = ConcurrentHashMap.newKeySet();
  private int largestPrime = 2;

  @Override
  public PrimesResponse getPrimes(final int initial) {
    if (initial <= 1) {
      throw new IllegalArgumentException("Initial number must be greater than 0");
    }
    final Set<Integer> primes = generateIntegers(initial);
    for (int i = 2; i <= initial; i++) {
      eliminateMultiples(primes, i);
    }
    primesCache.addAll(primes);
    largestPrime = Math.max(largestPrime, Collections.max(primes).intValue());
    return new PrimesResponse(initial, primes);
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
    if (primesCache.isEmpty()) {
      ints.add(2);
      for (int i = 3; i <= max; i += 2) {
        ints.add(i);
      }
    } else {
      ints.addAll(primesCache);
      if (!ints.removeIf(prime -> prime > max)) {
        for (int i = largestPrime; i <= max; i += 2) {
          ints.add(i);
        }
      }
    }
    return ints;
  }

}
