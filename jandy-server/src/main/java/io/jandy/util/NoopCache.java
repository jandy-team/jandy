package io.jandy.util;

import com.google.common.cache.AbstractCache;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author JCooky
 * @since 2016-03-11
 */
public class NoopCache<K, V> extends AbstractCache<K, V> {
//  private HashMap<K, V> map;

//  public NoopCache() {
//    map = new HashMap<>();
//  }

//  public NoopCache(int initialCapacity) {
//    map = new HashMap<>(initialCapacity);
//  }

  @Nullable
  @Override
  public V getIfPresent(Object key) {
    return null;
  }

  @Override
  public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
    V value = getIfPresent(key);
    if (value == null) {
      try {
        value = valueLoader.call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    return value;
  }

  @Override
  public void put(K key, V value) {
  }
}
