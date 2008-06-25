package com.enigmastation.neuralnet.impl.resolvers;

import com.enigmastation.neuralnet.Resolver;
import com.enigmastation.neuralnet.KeyNotFoundException;

import java.util.Map;

import javolution.util.FastMap;

public class BaseResolver implements Resolver {
    int nextkey = 1;
    Map<String, Integer> keyIdMap = new FastMap<String, Integer>();
    Map<Integer, String> idKeyMap = new FastMap<Integer, String>();
    
    public boolean addKey(String key, int keyId) {
        if (keyIdMap.containsKey(key) || idKeyMap.containsKey(keyId)) {
            return false;
        }
        nextkey = keyId+1;
        keyIdMap.put(key, keyId);
        idKeyMap.put(keyId, key);

        return true;
    }
    
    public int addKey(String key) {
        if (keyIdMap.containsKey(key)) {
            return keyIdMap.get(key);
        }
        int keyId = nextkey++;
        keyIdMap.put(key, keyId);
        idKeyMap.put(keyId, key);
        return keyId;
    }

    public int getId(String key) {
        if(keyIdMap.containsKey(key)) {
            return keyIdMap.get(key);
        }
        throw new KeyNotFoundException("key not found in resolver: "+String.valueOf(key));
    }

    public String getKey(int id) {
        if(idKeyMap.containsKey(id)) {
            return idKeyMap.get(id);
        }
        throw new KeyNotFoundException("id not found in resolver: "+String.valueOf(id));
    }
}
