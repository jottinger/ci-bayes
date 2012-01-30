package com.enigmastation.ml.common.collections;

import java.io.Serializable;

public class ConstantValueProvider<K, V> implements ValueProvider<K, V> {
  final V defaultValue;

  public ConstantValueProvider(V defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public V getDefault(Object k) {
    return defaultValue;
  }
}
