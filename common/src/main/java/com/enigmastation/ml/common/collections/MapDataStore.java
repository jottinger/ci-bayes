package com.enigmastation.ml.common.collections;

import java.util.Iterator;
import java.util.Map;

public abstract class MapDataStore<K, V> {
    final synchronized public void persist(Map updates) {
        startBlock();
        Iterator it = updates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            persistEntry(pair);
            it.remove();
        }
        endBlock();
    }

    protected void startBlock() {
    }

    protected void endBlock() {
    }

    protected abstract void persistEntry(Map.Entry pair);
}
