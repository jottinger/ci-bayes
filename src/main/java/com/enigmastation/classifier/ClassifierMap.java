package com.enigmastation.classifier;

import java.util.concurrent.locks.ReentrantLock;
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
    private final ReentrantLock lock = new ReentrantLock();

    public void incrementCategory(String category) {
        lock.lock();
        try {
            // int i = 0;
            //try {
            //    i = get(category).intValue() + 1;
            //} catch (NullPointerException npe) {
            //    i = 1;
            //}

            if (containsKey(category)) {
                put(category,get(category)+1);
            } else {
                put(category, 1);
            }

            totalCount += 1;
            lock.unlock();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public double getTotalCount() {
        return totalCount;
    }
}
