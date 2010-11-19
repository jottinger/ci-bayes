package com.enigmastation.resolvers.impl;

import com.enigmastation.resolvers.Resolver;
import com.google.common.collect.MapMaker;

import java.util.Collections;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Dec 29, 2008
 * Time: 5:10:46 PM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public class MemoryResolver implements Resolver {
    Map<String, Integer> keys = new MapMaker().makeMap();
    Map<Integer, String> ids = new MapMaker().makeMap();
    int lastId = 0;

    protected MemoryResolver() {
    }

    public Map<String, Integer> getKeys() {
        return Collections.unmodifiableMap(keys);
    }

    public Map<Integer, String> getIds() {
        return Collections.unmodifiableMap(ids);
    }

    public boolean addKey(String key, int id) {
        if (ids.containsKey(id)) {
            return false;
        }
        ids.put(id, key);
        keys.put(key, id);
        return true;
    }

    public int addKey(String key) {
        int keyId = 0;
        while (!addKey(key, keyId = ++lastId)) {
        }
        return keyId;
    }

    public int getId(String key) {
        if (keys.containsKey(key)) {
            return keys.get(key);
        }
        return -1;
    }

    public int getIdForKey(String key) {
        if (getId(key) == -1) {
            return addKey(key);
        }
        return getId(key);
    }

    public String getKey(int id) {
        if (ids.containsKey(id)) {
            return ids.get(id);
        }
        return null;
    }
}