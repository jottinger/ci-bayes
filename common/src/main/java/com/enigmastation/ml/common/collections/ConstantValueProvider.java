package com.enigmastation.ml.common.collections;

import java.util.Map;

public class ConstantValueProvider<K, V> implements ValueProvider<K, V> {
  final V defaultValue;

  public ConstantValueProvider(V defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public V getDefault(Object k) {
    return defaultValue;
  }

  @Override
  public Map<K, V> load() {
    return null;
  }
}
