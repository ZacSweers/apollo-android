package com.apollographql.apollo;

import com.apollographql.android.impl.normalizer.HeroAndFriendsNames;
import com.apollographql.android.impl.normalizer.type.Episode;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.cache.normalized.CacheControl;
import com.apollographql.apollo.cache.normalized.CacheHeaderSpec;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCache;
import com.apollographql.apollo.cache.normalized.Record;
import com.apollographql.apollo.exception.ApolloException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;

public class CacheHeadersTest {

  private ApolloClient apolloClient;
  private MockWebServer server;

  @Before
  public void setUp() {
    server = new MockWebServer();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testHeadersReceived() throws ApolloException, IOException {
    final NormalizedCache normalizedCache = new NormalizedCache() {
      @Nullable @Override public Record loadRecord(String key, Map<String, String> cacheHeaders) {
        assertThat(cacheHeaders.containsKey(CacheHeaderSpec.DO_NOT_CACHE)).isTrue();
        return null;
      }

      @Nonnull @Override public Set<String> merge(Record record, Map<String, String> cacheHeaders) {
        assertThat(cacheHeaders.containsKey(CacheHeaderSpec.DO_NOT_CACHE)).isTrue();
        return null;
      }

      @Override public void clearAll() {

      }
    };

    ApolloClient apolloClient = ApolloClient.builder().normalizedCache(normalizedCache, new CacheKeyResolver<Map<String, Object>>() {
      @Nonnull @Override public CacheKey resolve(@Nonnull Map<String, Object> objectSource) {
        return CacheKey.NO_KEY;
      }
    }).serverUrl(server.url("/"))
        .okHttpClient(new OkHttpClient())
        .build();


    server.enqueue(mockResponse("HeroAndFriendsNameResponse.json"));
    Map<String, String> cacheHeaders = new HashMap<>();
    cacheHeaders.put(CacheHeaderSpec.DO_NOT_CACHE, "true");
    final Response<HeroAndFriendsNames.Data> response
        = apolloClient.newCall(new HeroAndFriendsNames(Episode.NEWHOPE))
        .cacheControl(cacheHeaders)
        .execute();
  }

  @Test
  public void testDefaultHeadersReceived() throws IOException, ApolloException {
    final NormalizedCache normalizedCache = new NormalizedCache() {
      @Nullable @Override public Record loadRecord(String key, Map<String, String> cacheHeaders) {
        assertThat(cacheHeaders.containsKey(CacheHeaderSpec.DO_NOT_CACHE)).isTrue();
        return null;
      }

      @Nonnull @Override public Set<String> merge(Record record, Map<String, String> cacheHeaders) {
        assertThat(cacheHeaders.containsKey(CacheHeaderSpec.DO_NOT_CACHE)).isTrue();
        return null;
      }

      @Override public void clearAll() {

      }
    };

    Map<String, String> cacheHeaders = new HashMap<>();
    cacheHeaders.put(CacheHeaderSpec.DO_NOT_CACHE, "true");

    ApolloClient apolloClient = ApolloClient.builder().normalizedCache(normalizedCache, new CacheKeyResolver<Map<String, Object>>() {
      @Nonnull @Override public CacheKey resolve(@Nonnull Map<String, Object> objectSource) {
        return CacheKey.NO_KEY;
      }
    }).serverUrl(server.url("/"))
        .okHttpClient(new OkHttpClient())
        .defaultCacheControl(CacheControl.CACHE_FIRST, cacheHeaders)
        .build();


    server.enqueue(mockResponse("HeroAndFriendsNameResponse.json"));

    final Response<HeroAndFriendsNames.Data> response
        = apolloClient.newCall(new HeroAndFriendsNames(Episode.NEWHOPE))
        .execute();
  }

  private MockResponse mockResponse(String fileName) throws IOException {
    return new MockResponse().setChunkedBody(Utils.readFileToString(getClass(), "/" + fileName), 32);
  }

}
