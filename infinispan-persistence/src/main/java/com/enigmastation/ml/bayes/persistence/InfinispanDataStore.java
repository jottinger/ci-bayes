package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.common.collections.MapDataStore;
import com.enigmastation.ml.common.collections.ValueProvider;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

import java.util.Map;

public class InfinispanDataStore<K, V>
        extends MapDataStore<K, V>
        implements ValueProvider<K, V> {
    private String cacheName;
    private Cache<K, V> cache = null;

    private synchronized void connect() {
        if (cache == null) {
            cache = new DefaultCacheManager().getCache(getCacheName(), true);
        }
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    protected void persistEntry(Map.Entry<K, V> pair) {
        connect();
        cache.put(pair.getKey(), pair.getValue());
    }

    @Override
    public V getDefault(Object k) {
        connect();
        return cache.get(k);
    }
}
