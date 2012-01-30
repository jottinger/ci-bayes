package com.enigmastation.ml.common.collections;

public interface ValueProvider<K, V> {
  V getDefault(Object k);
}
