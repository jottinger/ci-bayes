package com.enigmastation.classifier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassifierMap
 * <p/>
 * With FastMap, it clocks in at 160MB max usage with the training corpus.
 * With TObjectIntHashMap&lt;String&gt;, it's only SLIGHTLY less, not enough to count.
 * Something tells me (Joe) that the stemming is having a larger effect here. But let's stick with Trove
 * anyway, just because. 
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph Ottinger</a>
 * @version $Revision: 36 $
 */

public final class ClassifierMap extends ConcurrentHashMap<String,Integer> { //implements Map<String, Integer> {
    private long totalCount;

    public void incrementCategory(String category, Integer amount) {
        if (containsKey(category)) {
            put(category,get(category)+amount);
        } else {
            put(category, amount);
        }

        totalCount += amount;
    }

    public void incrementCategory(String category) {
        incrementCategory(category, 1);
    }

    public double getTotalCount() {
        return totalCount;
    }
}
