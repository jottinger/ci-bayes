package com.enigmastation.ml.common.collections;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.Map;

public class MapWithDefaultValue<K, V>
        extends NonBlockingHashMap<K, V> {
    ValueProvider<K, V> valueProvider;
    Map<K, V> updates = new NonBlockingHashMap<>();
    int syncBlockSize = 0;
    MapDataStore<K, V> dataStore;

    public void init() {
        Map<K, V> load = valueProvider.load();
        if (load != null) {
            this.putAll(load);
        }
    }

    public int getSyncBlockSize() {
        return syncBlockSize;
    }

    public void setSyncBlockSize(int syncBlockSize) {
        this.syncBlockSize = syncBlockSize;
        checkForSync();
    }

    public MapDataStore<K, V> getDataStore() {
        return dataStore;
    }

    public void setDataStore(MapDataStore<K, V> dataStore) {
        this.dataStore = dataStore;
    }

    public MapWithDefaultValue(ValueProvider<K, V> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public MapWithDefaultValue(int initial_sz, ValueProvider<K, V> valueProvider) {
        super(initial_sz);
        this.valueProvider = valueProvider;
    }

    public V get(Object key) {
        V object = super.get(key);
        if (object == null) {
            object = valueProvider.getDefault(key);
        }
        return object;
    }

    @Override
    public V put(K key, V val) {
        updates.put(key, val);
        checkForSync();
        return super.put(key, val);
    }

    private void checkForSync() {
        if (updates.size() > syncBlockSize) {
            synchronize();
        }
    }

    public void synchronize() {
        boolean t = dataStore.persistAll(this);
        if (t) {
            dataStore.persist(updates);
        } else {
            // since persistAll() says it saved everything, just empty the journal
            updates.clear();
        }
    }
}
