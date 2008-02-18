package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;

public final class ClassifierMap extends FastMap<String, Integer> implements Map<String, Integer> {
    private long totalCount;

    public void incrementCategory(String category) {
        int i=0;
        try { 
            i=get(category).intValue()+1;
        } catch(NullPointerException npe) {
            i=1;
        }
        put(category, i);
        totalCount+=1;
    }

    public double getTotalCount() {
        return totalCount;
    }
}
