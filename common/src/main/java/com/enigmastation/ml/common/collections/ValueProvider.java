package com.enigmastation.ml.common.collections;

import java.util.Map;

public interface ValueProvider<K, V> {
  V getDefault(Object k);

  Map<K, V> load();
}
