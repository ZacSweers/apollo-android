package com.apollographql.apollo.cache.normalized.lru;

import com.apollographql.apollo.cache.normalized.NormalizedCache;
import com.apollographql.apollo.cache.normalized.Record;
import com.apollographql.apollo.cache.normalized.RecordFieldAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;

public class LruNormalizedCacheTest {

  private RecordFieldAdapter basicFieldAdapter;

  @Before public void createFieldAdapter() {
    basicFieldAdapter = RecordFieldAdapter.create(new Moshi.Builder().build());
  }

  @Test
  public void testEvictionPolicyBuilder() {
    final EvictionPolicy policy = EvictionPolicy.builder()
        .maxSizeBytes(100)
        .maxEntries(50)
        .expireAfterAccess(5, TimeUnit.HOURS)
        .expireAfterWrite(10, TimeUnit.DAYS)
        .build();

    assertThat(policy.maxSizeBytes().get()).isEqualTo(100);

    assertThat(policy.maxEntries().get()).isEqualTo(50);

    assertThat(policy.expireAfterAccess().get()).isEqualTo(5);
    assertThat(policy.expireAfterAccessTimeUnit().get()).isEqualTo(TimeUnit.HOURS);

    assertThat(policy.expireAfterWrite().get()).isEqualTo(10);
    assertThat(policy.expireAfterWriteTimeUnit().get()).isEqualTo(TimeUnit.DAYS);
  }

  @Test
  public void testSaveAndLoad_singleRecord() {
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build
        ()).createNormalizedCache(basicFieldAdapter);
    Record testRecord = createTestRecord("1");

<<<<<<< HEAD
    lruCache.merge(testRecord);
=======
    lruCacheStore.merge(testRecord, Collections.<String, String>emptyMap());
>>>>>>> WIP

    assertTestRecordPresentAndAccurate(testRecord, lruCache);
  }

  @Test
  public void testSaveAndLoad_multipleRecord_readSingle() {
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build
        ()).createNormalizedCache(basicFieldAdapter);
    Record testRecord1 = createTestRecord("1");
    Record testRecord2 = createTestRecord("2");
    Record testRecord3 = createTestRecord("3");

    List<Record> records = Arrays.asList(testRecord1, testRecord2, testRecord3);
<<<<<<< HEAD
    lruCache.merge(records);
=======
    lruCacheStore.merge(records, Collections.<String, String>emptyMap());
>>>>>>> WIP

    assertTestRecordPresentAndAccurate(testRecord1, lruCache);
    assertTestRecordPresentAndAccurate(testRecord2, lruCache);
    assertTestRecordPresentAndAccurate(testRecord3, lruCache);
  }

  @Test
  public void testSaveAndLoad_multipleRecord_readMultiple() {
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build
        ()).createNormalizedCache(basicFieldAdapter);
    Record testRecord1 = createTestRecord("1");
    Record testRecord2 = createTestRecord("2");
    Record testRecord3 = createTestRecord("3");

    List<Record> inputRecords = Arrays.asList(testRecord1, testRecord2, testRecord3);
<<<<<<< HEAD
    lruCache.merge(inputRecords);
    final Collection<Record> readRecords = lruCache.loadRecords(Arrays.asList("key1", "key2", "key3"));
=======
    lruCacheStore.merge(inputRecords, Collections.<String, String>emptyMap());
    final Collection<Record> readRecords = lruCacheStore.loadRecords(Arrays.asList("key1", "key2", "key3"),
        Collections.<String, String>emptyMap());
>>>>>>> WIP
    //noinspection ResultOfMethodCallIgnored
    assertThat(readRecords).containsExactlyElementsIn(inputRecords);
  }

  @Test
  public void testLoad_recordNotPresent() {
<<<<<<< HEAD
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build
        ()).createNormalizedCache(basicFieldAdapter);
    final Record record = lruCache.loadRecord("key1");
=======
    LruNormalizedCache lruCacheStore = new LruNormalizedCache(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build());
    final Record record = lruCacheStore.loadRecord("key1", Collections.<String, String>emptyMap());
>>>>>>> WIP
    assertThat(record).isNull();
  }

  @Test
  public void testEviction() {
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(2000)
        .build()).createNormalizedCache(basicFieldAdapter);

    Record.Builder testRecord1Builder = Record.builder("key1");
    testRecord1Builder.addField("a", new String(new byte[1100]));
    Record testRecord1 = testRecord1Builder.build();

    Record.Builder testRecord2Builder = Record.builder("key2");
    testRecord2Builder.addField("a", new String(new byte[1100]));
    Record testRecord2 = testRecord2Builder.build();

    Record.Builder testRecord3Builder = Record.builder("key3");
    testRecord3Builder.addField("a", new String(new byte[10]));
    Record testRecord3 = testRecord3Builder.build();

    List<Record> records = Arrays.asList(
        testRecord1,
        testRecord2,
        testRecord3
    );
<<<<<<< HEAD
    lruCache.merge(records);
=======
    lruCacheStore.merge(records, Collections.<String, String>emptyMap());
>>>>>>> WIP

    //Cache does not reveal exactly how it handles eviction, but appears
    //to evict more than is strictly necessary. Regardless, any sane eviction
    //strategy should leave the third record in this test case, and evict the first record.
<<<<<<< HEAD
    assertThat(lruCache.loadRecord("key1")).isNull();
    assertThat(lruCache.loadRecord("key3")).isNotNull();
=======
    assertThat(lruCacheStore.loadRecord("key1", Collections.<String, String>emptyMap())).isNull();
    assertThat(lruCacheStore.loadRecord("key3", Collections.<String, String>emptyMap())).isNotNull();
>>>>>>> WIP

  }

  @Test
  public void testEviction_recordChange() {
    LruNormalizedCache lruCache = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(2000)
        .build()).createNormalizedCache(basicFieldAdapter);

    Record.Builder testRecord1Builder = Record.builder("key1");
    testRecord1Builder.addField("a", new String(new byte[10]));
    Record testRecord1 = testRecord1Builder.build();

    Record.Builder testRecord2Builder = Record.builder("key2");
    testRecord2Builder.addField("a", new String(new byte[10]));
    Record testRecord2 = testRecord2Builder.build();

    Record.Builder testRecord3Builder = Record.builder("key3");
    testRecord3Builder.addField("a", new String(new byte[10]));
    Record testRecord3 = testRecord3Builder.build();

    List<Record> records = Arrays.asList(
        testRecord1,
        testRecord2,
        testRecord3
    );
<<<<<<< HEAD
    lruCache.merge(records);

    //All records should present
    assertThat(lruCache.loadRecord("key1")).isNotNull();
    assertThat(lruCache.loadRecord("key2")).isNotNull();
    assertThat(lruCache.loadRecord("key3")).isNotNull();
=======
    lruCacheStore.merge(records, Collections.<String, String>emptyMap());

    //All records should present
    assertThat(lruCacheStore.loadRecord("key1", Collections.<String, String>emptyMap())).isNotNull();
    assertThat(lruCacheStore.loadRecord("key2", Collections.<String, String>emptyMap())).isNotNull();
    assertThat(lruCacheStore.loadRecord("key3", Collections.<String, String>emptyMap())).isNotNull();
>>>>>>> WIP

    Record.Builder largeTestRecordBuilder = Record.builder("key1");
    largeTestRecordBuilder.addField("a", new String(new byte[2000]));
    Record largeTestRecord = largeTestRecordBuilder.build();

<<<<<<< HEAD
    lruCache.merge(largeTestRecord);
    //The large record (Record 1) should be evicted. the other small records should remain.
    assertThat(lruCache.loadRecord("key1")).isNull();
    assertThat(lruCache.loadRecord("key2")).isNotNull();
    assertThat(lruCache.loadRecord("key3")).isNotNull();
=======
    lruCacheStore.merge(largeTestRecord, Collections.<String, String>emptyMap());
    //The large record (Record 1) should be evicted. the other small records should remain.
    assertThat(lruCacheStore.loadRecord("key1", Collections.<String, String>emptyMap())).isNull();
    assertThat(lruCacheStore.loadRecord("key2", Collections.<String, String>emptyMap())).isNotNull();
    assertThat(lruCacheStore.loadRecord("key3", Collections.<String, String>emptyMap())).isNotNull();
>>>>>>> WIP

  }

  @Test
  public void testDualCacheSingleRecord() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    Record.Builder recordBuilder = Record.builder("root");
    recordBuilder.addField("bar", "bar");
    final Record record = recordBuilder.build();
    primaryCacheStore.merge(record, Collections.<String, String>emptyMap());

    //verify write through behavior
<<<<<<< HEAD
    assertThat(primaryCacheStore.loadRecord("root").field("bar")).isEqualTo("bar");
    assertThat(primaryCacheStore.secondaryCache().loadRecord("root").field("bar")).isEqualTo("bar");
=======
    assertThat(primaryCacheStore.loadRecord("root",
        Collections.<String, String>emptyMap()).field("bar")).isEqualTo("bar");
    assertThat(secondaryCacheStore.loadRecord("root",
        Collections.<String, String>emptyMap()).field("bar")).isEqualTo("bar");
>>>>>>> WIP
  }

  @Test
  public void testDualCacheMultipleRecord() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    Record.Builder recordBuilder = Record.builder("root1");
    recordBuilder.addField("bar", "bar");
    final Record record1 = recordBuilder.build();

    recordBuilder = Record.builder("root2");
    recordBuilder.addField("bar", "bar");
    final Record record2 = recordBuilder.build();

    recordBuilder = Record.builder("root3");
    recordBuilder.addField("bar", "bar");
    final Record record3 = recordBuilder.build();

    Collection<Record> records = Arrays.asList(record1, record2, record3);
    Collection<String> keys = Arrays.asList(record1.key(), record2.key(), record3.key());

    primaryCacheStore.merge(records, Collections.<String, String>emptyMap());

    assertThat(primaryCacheStore.loadRecords(keys, Collections.<String, String>emptyMap()).size()).isEqualTo(3);

    //verify write through behavior
<<<<<<< HEAD
    assertThat(primaryCacheStore.loadRecords(keys).size()).isEqualTo(3);
    assertThat(primaryCacheStore.secondaryCache().loadRecords(keys).size()).isEqualTo(3);
=======
    assertThat(primaryCacheStore.loadRecords(keys, Collections.<String, String>emptyMap()).size()).isEqualTo(3);
    assertThat(secondaryCacheStore.loadRecords(keys, Collections.<String, String>emptyMap()).size()).isEqualTo(3);
>>>>>>> WIP
  }

  @Test
  public void testDualCache_recordNotPresent() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    assertThat(primaryCacheStore.loadRecord("not_present_id", Collections.<String, String>emptyMap())).isNull();
  }

  @Test
  public void testClearAll() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    Record record = Record.builder("key").build();

    primaryCacheStore.merge(record, Collections.<String, String>emptyMap());
    primaryCacheStore.clearAll();

    assertThat(primaryCacheStore.loadRecord("key", Collections.<String, String>emptyMap())).isNull();
  }

  @Test
  public void testClearPrimaryCache() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    Record record = Record.builder("key").build();

    primaryCacheStore.merge(record, Collections.<String, String>emptyMap());
    primaryCacheStore.clearPrimaryCache();

<<<<<<< HEAD
    assertThat(primaryCacheStore.secondaryCache().loadRecord("key")).isNotNull();
=======
    assertThat(secondaryCacheStore.loadRecord("key", Collections.<String, String>emptyMap())).isNotNull();
>>>>>>> WIP
  }

  @Test
  public void testClearSecondaryCache() {
    LruNormalizedCacheFactory secondaryCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION);
    LruNormalizedCache primaryCacheStore = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
        secondaryCacheFactory).createNormalizedCache(basicFieldAdapter);

    Record record = Record.builder("key").build();

    primaryCacheStore.merge(record, Collections.<String, String>emptyMap());
    primaryCacheStore.clearSecondaryCache();

<<<<<<< HEAD
    assertThat(primaryCacheStore.secondaryCache().loadRecord("key")).isNull();
=======
    assertThat(secondaryCacheStore.loadRecord("key", Collections.<String, String>emptyMap())).isNull();
>>>>>>> WIP
  }

  private void assertTestRecordPresentAndAccurate(Record testRecord, NormalizedCache store) {
    final Record cacheRecord1 = store.loadRecord(testRecord.key(), Collections.<String, String>emptyMap());
    assertThat(cacheRecord1.key()).isEqualTo(testRecord.key());
    assertThat(cacheRecord1.field("a")).isEqualTo(testRecord.field("a"));
    assertThat(cacheRecord1.field("b")).isEqualTo(testRecord.field("b"));
  }

  private Record createTestRecord(String id) {
    Record.Builder testRecord = Record.builder("key" + id);
    testRecord.addField("a", "stringValueA" + id);
    testRecord.addField("b", "stringValueB" + id);
    return testRecord.build();
  }
}
