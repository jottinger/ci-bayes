package com.enigmastation.ml.common.collections;

import java.util.Iterator;
import java.util.Map;

public abstract class MapDataStore<K, V> {
  public void persist(Map updates) {
    Iterator it = updates.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      persistEntry(pair);
      it.remove();
    }
  }

  protected abstract void persistEntry(Map.Entry pair);
}
