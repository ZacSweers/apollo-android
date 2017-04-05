package com.apollographql.apollo.cache.normalized;

import com.apollographql.apollo.ApolloClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A provider of {@link Record} for reading requests from cache.
 *
 * If {@link NormalizedCache#loadRecords(Collection, Map)} returns an empty set while, the request will be considered a
 * cache-miss.
 *
<<<<<<< HEAD
 * To serialize a {@link Record} to a standardized form use {@link #recordAdapter()} which handles
 * call custom scalar types registered on the {@link ApolloClient}.
=======
 * A {@link NormalizedCache} is recommended to implement support for CacheHeaders specified in {@link CacheHeaderSpec}.
>>>>>>> WIP
 *
 * A {@link NormalizedCache} can choose to store records in any manner.
 * See {@link com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCache} for a persistent cache.
 * See {@link com.apollographql.apollo.cache.normalized.lru.LruNormalizedCache} for a in memory cache.
 */
public abstract class NormalizedCache {

  private RecordFieldAdapter recordFieldAdapter;

  /**
   * @param recordFieldAdapter An adapter which can deserialize and deserialize {@link Record}
   */
  public NormalizedCache(RecordFieldAdapter recordFieldAdapter) {
    this.recordFieldAdapter = recordFieldAdapter;
  }

  protected RecordFieldAdapter recordAdapter() {
    return recordFieldAdapter;
  }

  /**
   * @param key          The key of the record to read
   * @param cacheHeaders The cache headers associated with the request which generated this record.
   * @return The {@link Record} for key. If not present return null.
   */
  @Nullable public abstract Record loadRecord(String key, Map<String, String> cacheHeaders);

  /**
   * Calls through to {@link NormalizedCache#loadRecord(String, Map)}. Implementations should override this method if
   * the
   * underlying storage technology can offer an optimized manner to read multiple records.
   *
   * @param keys         The set of {@link Record} keys to read.
   * @param cacheHeaders The cache headers associated with the request which generated this record.
   */
  @Nonnull public Collection<Record> loadRecords(Collection<String> keys, Map<String, String> cacheHeaders) {
    List<Record> records = new ArrayList<>(keys.size());
    for (String key : keys) {
      final Record record = loadRecord(key, cacheHeaders);
      if (record != null) {
        records.add(record);
      }
    }
    return records;
  }

  /**
   * @param record       The {@link Record} to merge.
   * @param cacheHeaders The cache headers associated with the request which generated this record.
   * @return A set of record field keys that have changed. This set is returned by {@link Record#mergeWith(Record)}.
   */
  @Nonnull public abstract Set<String> merge(Record record, Map<String, String> cacheHeaders);

  /**
   * Calls through to {@link NormalizedCache#merge(Record, Map)}. Implementations should override this
   * method if the
   * underlying storage technology can offer an optimized manner to store multiple records.
   *
   * @param recordSet    The set of Records to merge
   * @param cacheHeaders The cache headers associated with the request which generated this record.
   * @return A set of record field keys that have changed. This set is returned by {@link Record#mergeWith(Record)}.
   */
  @Nonnull public Set<String> merge(Collection<Record> recordSet, Map<String, String> cacheHeaders) {
    Set<String> aggregatedDependentKeys = new LinkedHashSet<>();
    for (Record record : recordSet) {
      aggregatedDependentKeys.addAll(merge(record, cacheHeaders));
    }
    return aggregatedDependentKeys;
  }

  /**
   * Clears all records from the cache.
   *
   * Clients should call {@link ApolloClient#clearNormalizedCache()} for a thread-safe access to
   * this method.
   */
  public abstract void clearAll();

}
