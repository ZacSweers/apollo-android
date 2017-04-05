package com.apollographql.apollo.cache.normalized;

/**
 * A collection of "standardized" cache headers that an implementation {@link NormalizedCache}
 * is recommended to respect.
 */
public class CacheHeaderSpec {

  /**
   * Records from this request should not be stored in the {@link NormalizedCache}.
   * This does **not** specify that a request should not be read from a cache.
   */
  public static final String DO_NOT_CACHE = "do-not-cache";
}
