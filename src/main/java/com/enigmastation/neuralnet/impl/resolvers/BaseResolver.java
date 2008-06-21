package com.enigmastation.neuralnet.impl.resolvers;

import com.enigmastation.neuralnet.Resolver;
import com.enigmastation.neuralnet.KeyNotFoundError;

import java.util.Map;

import javolution.util.FastMap;

public class BaseResolver<T> implements Resolver<T> {
    int nextkey = 0;
    Map<T, Integer> keyIdMap = new FastMap<T, Integer>();
    Map<Integer, T> idKeyMap = new FastMap<Integer, T>();

    public boolean addKey(T key, int keyId) {
        if (keyIdMap.containsKey(key) || idKeyMap.containsKey(keyId)) {
            return false;
        }
        nextkey = keyId+1;
        keyIdMap.put(key, keyId);
        idKeyMap.put(keyId, key);

        return true;
    }
    
    public int addKey(T key) {
        if (keyIdMap.containsKey(key)) {
            return keyIdMap.get(key);
        }
        int keyId = nextkey++;
        keyIdMap.put(key, keyId);
        idKeyMap.put(keyId, key);
        return keyId;
    }

    public int getId(T key) {
        if(keyIdMap.containsKey(key)) {
            return keyIdMap.get(key);
        }
        throw new KeyNotFoundError("key not found in resolver: "+String.valueOf(key));
    }

    public T getKey(int id) {
        if(idKeyMap.containsKey(id)) {
            return idKeyMap.get(id);
        }
        throw new KeyNotFoundError("id not found in resolver: "+String.valueOf(id));
    }
}
