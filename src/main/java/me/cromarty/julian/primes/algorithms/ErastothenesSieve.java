package me.cromarty.julian.primes.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
  private final Set<Integer> primesCache = Collections.synchronizedSortedSet(new TreeSet<Integer>());
  private int largestNumberChecked;

  public ErastothenesSieve() {
    /* pre-calculate primes up to a thousand to save time */
    primesCache.add(2);
    primesCache.add(3);
    primesCache.add(5);
    primesCache.add(7);
    largestNumberChecked = 9;
    findPrimes(1000);
  }

  /**
   * Returns primes up to the requested value from either the cache or by
   * calculating them.
   */
  @Override
  public PrimesResponse getPrimes(final int initial) {
    if (initial <= 1) {
      throw new IllegalArgumentException("Initial number must be greater than 0");
    }
    if (largestNumberChecked < initial) {
      findPrimes(initial);
    }

    final Set<Integer> primes = new TreeSet<>();
    synchronized (primesCache) {
      for (final Integer i : primesCache) {
        if (i > initial) {
          break;
        }
        primes.add(i);
      }
    }
    return new PrimesResponse(initial, primes);
  }

  /**
   * Checks through all odd numbers not yet in the primes cache and tests them for
   * primality, adding them to the cache as needed
   *
   * If the largest number tested so far is greater than the square root of the
   * maximum number to check for, the work is parallelised as all the primes
   * needed for checking primality are already in the cache. To increase speed,
   * the test to see if the loop can be parallelised is repeated after every 500
   * numbers checked so that we can parallelise as soon as possible.
   *
   * Synchronized so that no work is done twice, as all found primes go into the
   * cache for future calls
   *
   * @param max
   */
  private synchronized void findPrimes(final int max) {
    final List<Future<?>> tasks = new ArrayList<>();

    while (largestNumberChecked <= max) {
      final boolean parallel = (largestNumberChecked * largestNumberChecked) > max;
      final int loopMax = largestNumberChecked + 1000;
      for (int i = largestNumberChecked; i <= loopMax; i += 2) {
        final int potentialPrime = i;
        if (parallel) {
          tasks.add(executor.submit(() -> {
            checkPrimality(potentialPrime);
          }));
        } else {
          checkPrimality(potentialPrime);
        }
      }
      if (parallel) {
        waitForCompletion(tasks);
      }
      largestNumberChecked = (loopMax % 2) == 0 ? loopMax - 1 : loopMax;
    }
  }

  /**
   * Checks to see if the given number is prime by dividing it by all primes that
   * we already know about that are less than its square root
   *
   * @param potentialPrime
   */
  private void checkPrimality(final int potentialPrime) {
    boolean isPrime = true;
    for (final int prime : primesCache) {
      if (((potentialPrime % prime) == 0) && (potentialPrime > prime)) {
        isPrime = false;
        break;
      }
      if ((prime * prime) > potentialPrime) {
        break;
      }
    }
    if (isPrime) {
      synchronized (primesCache) {
        primesCache.add(potentialPrime);
      }
    }
  }

  /**
   * Wait for all the {@link Future}s spawned by a loop to finish
   *
   * @param tasks
   *          List of futures to check
   */
  private void waitForCompletion(final List<Future<?>> tasks) {
    while (!tasks.isEmpty()) {
      final List<Future<?>> doneTasks = new ArrayList<>();
      for (final Future<?> f : tasks) {
        if (f.isDone()) {
          doneTasks.add(f);
        }
      }
      tasks.removeAll(doneTasks);
      try {
        /* Don't thrash the CPU checking for finished futures */
        Thread.sleep(10);
      } catch (final InterruptedException ignored) {
      }
    }
  }

}
