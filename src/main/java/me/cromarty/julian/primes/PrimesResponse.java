package me.cromarty.julian.primes;

import java.util.Set;

import lombok.Data;

/**
 * Data object to hold the result of finding primes
 *
 * @author Julian Cromarty
 */
@Data
public class PrimesResponse {
  private final int initial;
  private final Set<Integer> primes;
}
