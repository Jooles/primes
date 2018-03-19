package me.cromarty.julian.primes.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.cromarty.julian.primes.PrimesFinder;
import me.cromarty.julian.primes.PrimesResponse;

/**
 * Naive implementation of a Sieve of Erastothenes for finding primes
 *
 * @author Julian Cromarty
 */
public class ErastothenesSieve implements PrimesFinder {

  private final ExecutorService executor = Executors.newWorkStealingPool();
  private final Set<Integer> primesCache = ConcurrentHashMap.newKeySet();
  private int largestPrime = 2;

  @Override
  public PrimesResponse getPrimes(final int initial) {
    if (initial <= 1) {
      throw new IllegalArgumentException("Initial number must be greater than 0");
    }
    Set<Integer> primes;
    if (primesCache.isEmpty()) {
      primes = generateIntegers(initial);
      eliminateMultiples(primes, initial);
      primesCache.addAll(primes);
      largestPrime = Math.max(largestPrime, Collections.max(primes).intValue());
    } else if (largestPrime >= initial) {
      primes = new TreeSet<>(primesCache);
      primes.removeIf(i -> i > initial);
    } else {
      primes = generateAdditionalIntegers(largestPrime, initial);
      eliminateMultiples(primes, initial);
      primesCache.addAll(primes);
      largestPrime = Math.max(largestPrime, Collections.max(primes).intValue());
      primes = primesCache;
    }

    return new PrimesResponse(initial, primes);
  }

  private void eliminateMultiples(final Set<Integer> ints, final int initial) {
    final List<Future<?>> tasks = new ArrayList<>();
    for (final Integer i : ints) {
      tasks.add(executor.submit(() -> {
        final int max = Math.max(i, initial);
        for (int multiple = 3; multiple <= max; multiple++) {
          if (((i % multiple) == 0) && (i > multiple)) {
            ints.remove(i);
            break;
          }
        }
      }));
    }
    while (!tasks.isEmpty()) {
      final List<Future<?>> doneTasks = new ArrayList<>();
      for (final Future<?> f : tasks) {
        if (f.isDone()) {
          doneTasks.add(f);
        }
      }
      tasks.removeAll(doneTasks);
    }
  }

  private Set<Integer> generateIntegers(final int max) {
    final Set<Integer> ints = ConcurrentHashMap.newKeySet();
    ints.add(2);
    for (int i = 3; i <= max; i += 2) {
      ints.add(i);
    }
    return ints;
  }

  private Set<Integer> generateAdditionalIntegers(final int min, final int max) {
    final Set<Integer> ints = ConcurrentHashMap.newKeySet();
    for (int i = min; i <= max; i += 2) {
      ints.add(i);
    }
    return ints;
  }

}
