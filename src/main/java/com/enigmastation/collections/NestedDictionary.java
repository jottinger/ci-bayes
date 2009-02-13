package com.enigmastation.collections;

import javolution.util.FastMap;
import javolution.util.FastComparator;

import java.util.Map;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jan 23, 2009
 * Time: 10:30:04 AM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public abstract class NestedDictionary<T, Y> extends FastMap<T, Map<T, Y>> implements Map<T, Map<T, Y>> {
    Class<T> keyType = null;
    Y defaultValue = null;

    public NestedDictionary(Y defaultValue) {
        this();
        this.defaultValue = defaultValue;
    }

    public Y getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Y defaultValue) {
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings({"unchecked"})
    public NestedDictionary() {
        super();
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        keyType = (Class<T>) type.getClass();
        setKeyComparator(new FastComparator<T>() {
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
                if (o instanceof Comparable) {
                    return ((Comparable) o).compareTo(o1);
                }
                return 0;
            }
        });
    }

    public void put(T key, T subKey, Y d) {
        Map<T, Y> subMap = get(key);
        if (subMap == null) {
            FastMap rMap = new FastMap<T, Y>();
            //noinspection unchecked
            rMap.setKeyComparator(this.getKeyComparator());
            //noinspection unchecked
            subMap = rMap;

            put(key, subMap);
        }

        if (subMap.containsKey(subKey)) {
            subMap.remove(subKey);
        }
        subMap.put(subKey, d);
    }

    public Map<T, Y> load(T key) {
        Map<T, Y> map = get(key);
        if (map == null) {
            map = new FastMap<T, Y>();
        }
        return map;
    }

    public Y get(T key, T subKey) {
        Map<T, Y> map = load(key);
        Y value = map.get(subKey);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
}
