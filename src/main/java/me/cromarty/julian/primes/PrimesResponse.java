package me.cromarty.julian.primes;

import java.util.Set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import lombok.Data;

/**
 * Data object to hold the result of finding primes
 *
 * @author Julian Cromarty
 */
@Data
public class PrimesResponse {
  private final int initial;
  @JacksonXmlElementWrapper(useWrapping=false)
  private final Set<Integer> primes;
}
