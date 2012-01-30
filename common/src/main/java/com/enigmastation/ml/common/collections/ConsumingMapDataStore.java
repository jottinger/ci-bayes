package com.enigmastation.ml.common.collections;

import java.util.Map;

public class ConsumingMapDataStore<K, V> extends MapDataStore<K, V> {
  @Override
  protected void persistEntry(Map.Entry pair) {
    // discards pair
  }
}
