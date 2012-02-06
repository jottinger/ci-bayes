package com.enigmastation.ml.common.collections;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class MapTest {
  @Test
  public void testBasicMap() {
    Map<String, Integer> map = new MapBuilder().build();
    assertNull(map.get("foo"));
    map.put("foo", 1);
    assertEquals(map.get("foo"), new Integer(1));
  }

  @Test
  public void testMapProvidedValue() {
    Map<String, String> map = new MapBuilder()
                              .valueProvider(new ValueProvider<String, String>() {
                                @Override
                                public String getDefault(Object k) {
                                  String key = k.toString();
                                  StringBuilder sb = new StringBuilder();
                                  for (char c : key.toCharArray()) {
                                    sb.insert(0, c);

                                  }
                                  return sb.toString();
                                }

                                @Override
                                public Map<String, String> load() {
                                  return null;
                                }
                              }).build();
    map.put("bar", "baz");
    assertEquals(map.get("foo"), "oof");
    assertEquals(map.get("foo"), "oof");
    assertEquals(map.get("bar"), "baz");
  }

  @Test
  public void testMapSync() {
    final Map<Object, Object> updatedValues = new HashMap<Object, Object>();
    Map<String, String> map = new MapBuilder()
                              .dataStore(new MapDataStore<String, String>() {
                                @Override
                                protected void persistEntry(Map.Entry pair) {
                                  System.out.println(pair);
                                  updatedValues.put(pair.getKey(), pair.getValue());
                                }
                              })
                              .build();
    map.put("foo", "bar");
    assertEquals(updatedValues.get("foo"), "bar");
  }

  @Test
  public void testMapSyncWithBlockSize() {
    final Map<Object, Object> updatedValues = new HashMap<Object, Object>();
    System.out.println("---------------");
    Map<String, String> map = new MapBuilder()
                              .syncBlockSize(4)
                              .dataStore(new MapDataStore<String, String>() {
                                @Override
                                protected void persistEntry(Map.Entry pair) {
                                  System.out.println(pair);
                                  updatedValues.put(pair.getKey(), pair.getValue());
                                }
                              })
                              .build();
    map.put("foo", "bar");
    assertNull(updatedValues.get("foo"));
    for (int i = 0; i < 5; i++) {
      map.put(Integer.toString(i), Integer.toString(i));
    }
    assertEquals(updatedValues.get("foo"), "bar");
    assertEquals(updatedValues.get("3"), "3");
    // why null? Because the map is discarded before the sync is run.
    // how to address?
    // probably a finalizer.
    assertNull(updatedValues.get("4"));
  }

  @Test
  public void testMapWithDouble() {
    Map<String, Double> map = new MapBuilder().defaultValue(1.0).build();
    assertEquals(map.get("foo"), 1.0, 0.01);
  }
}
