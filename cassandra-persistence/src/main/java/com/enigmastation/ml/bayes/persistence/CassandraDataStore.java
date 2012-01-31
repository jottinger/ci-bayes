package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.common.collections.MapDataStore;
import com.enigmastation.ml.common.collections.ValueProvider;

import java.util.Map;

public class CassandraDataStore extends MapDataStore implements ValueProvider {
    @Override
    protected void persistEntry(Map.Entry pair) {
        // in a pair, examine the value; if it's a map, translate each entry into a column
        // the column key is the pair's key
        // a type indicator is used to indicate the actual type ("feature" or "category")
    }

    @Override
    public Object getDefault(Object k) {
        // load a row with key k
        // if the row has a type of "feature" then create a map and return that, otherwise it's a simple object and is returned as such
        return null;
    }
}
