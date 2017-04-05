package com.apollographql.apollo.internal.cache.normalized;

import com.apollographql.apollo.cache.normalized.Record;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ReadableCache {

  @Nullable Record read(@Nonnull String key, Map<String, String> cacheHeaders);

  Collection<Record> read(@Nonnull Collection<String> keys, Map<String, String> cacheHeaders);

}
