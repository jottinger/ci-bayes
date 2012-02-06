package com.enigmastation.ml.common.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class MapDataStore<K, V> implements ValueProvider<K, V> {
  final synchronized public void persist(Map<K, V> updates) {
    startBlock();
    Iterator<Map.Entry<K, V>> it = updates.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<K, V> pair = it.next();
      persistEntry(pair);
      it.remove();
    }
    endBlock();
  }

  /**
   * Persists the entire data structure as a whole, if necessary.
   * <p/>
   * If the data structure is incomplete (i.e., represents a snapshot and not the actual
   * dataset) this method can corrupt the data!
   *
   * @param dataStructure the underlying data structure as a whole
   * @return false if data structure has been persisted
   */
  @SuppressWarnings("UnusedParameters")
  public boolean persistAll(Map<K, V> dataStructure) {
    return true;
  }

  protected void startBlock() {
  }

  protected void endBlock() {
  }

  protected void persistEntry(Map.Entry<K, V> pair) {
  }

  @Override
  public V getDefault(Object k) {
    return null;
  }

  @Override
  public Map<K, V> load() {
    return new HashMap<K, V>();
  }
}
