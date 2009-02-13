package com.enigmastation.collections;

import javolution.util.FastComparator;
import javolution.util.FastMap;

import java.util.Map;

public class NestedDictionaryStringStringDouble extends FastMap<Object, Map<Object, Double>> {
    public NestedDictionaryStringStringDouble() {
        super();
        setKeyComparator(new FastComparator<Object>() {
            public int hashCodeOf(Object o) {
                if (o.getClass().equals(String.class)) {
                    return ((String) o).toLowerCase().hashCode();
                }
                return o.hashCode();
            }

            public boolean areEqual(Object o, Object o1) {
                return hashCodeOf(o) == hashCodeOf(o1);
            }

            public int compare(Object o, Object o1) {
                if (o.getClass().equals(String.class) && o1.getClass().equals(String.class)) {
                    String s = (String) o;
                    String s1 = (String) o1;
                    return s.toLowerCase().compareTo(s1.toLowerCase());
                }
                return 0;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void save(Object key, Object subKey, Double d) {
        Map<Object, Double> subMap = get(key);
        if (subMap == null) {
            FastMap rMap = new FastMap<Object, Double>();
            rMap.setKeyComparator(this.getKeyComparator());
            subMap = rMap;

            put(key, subMap);
        }

        if (subMap.containsKey(subKey)) {
            subMap.remove(subKey);
        }
        subMap.put(subKey, d);
    }

    public Map<Object, Double> load(Object key) {
        Map<Object, Double> map = get(key);
        if (map == null) {
            map = new FastMap<Object, Double>();
        }
        return map;
    }
}
