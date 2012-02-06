package com.enigmastation.ml.common.collections;

import java.util.Map;

public class MapBuilder {
  private ValueProvider valueProvider = null;
  private int syncBlockSize = -1;
  private MapDataStore<?, ?> dataStore = null;

  public <K, V> MapBuilder dataStore(MapDataStore<K, V> dataStore) {
    if (this.dataStore != null) {
      throw new IllegalStateException("dataStore already set");
    }
    this.dataStore = dataStore;
    return this;
  }

  public MapBuilder syncBlockSize(int size) {
    if (syncBlockSize != -1) {
      throw new IllegalStateException("syncBlockSize already set");
    }
    this.syncBlockSize = size;
    return this;
  }

  public <V> MapBuilder defaultValue(V defaultValue) {
    checkValueProviderState();
    this.valueProvider = new ConstantValueProvider<Object, V>(defaultValue);
    return this;
  }

  public <K, V> MapBuilder valueProvider(ValueProvider<K, V> valueProvider) {
    checkValueProviderState();
    this.valueProvider = valueProvider;
    return this;
  }

  private <K, V> void checkValueProviderState() {
    if (valueProvider != null) {
      throw new IllegalStateException("valueProvider already set");
    }
  }

  public <K, V> Map<K, V> build() {
    if (valueProvider == null) {
      valueProvider = new ConstantValueProvider<K, V>(null);
    }
    //noinspection unchecked
    MapWithDefaultValue<K, V> map = new MapWithDefaultValue<K, V>((ValueProvider<K, V>) valueProvider);
    if (syncBlockSize != -1) {
      map.setSyncBlockSize(syncBlockSize);
    }
    if (dataStore == null) {
      dataStore = new ConsumingMapDataStore<>();
    }
    //noinspection unchecked
    map.setDataStore((MapDataStore<K, V>) dataStore);
    map.init();
    return map;
  }
}
