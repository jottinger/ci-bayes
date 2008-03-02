package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassifierMap
 *
 * With FastMap, it clocks in at 160MB max usage with the training corpus.
 * @author <a href="mailto:joeo@enigmastation.com">Joseph Ottinger</a>
 * @version $Revision$
 */

public final class ClassifierMap extends FastMap<String, Integer> implements Map<String, Integer> {
    private long totalCount;
    private final ReentrantLock lock = new ReentrantLock();

    public void incrementCategory(String category) {
        lock.lock();
        try {
            int i = 0;
            try {
                i = get(category).intValue() + 1;
            } catch (NullPointerException npe) {
                i = 1;
            }
            put(category, i);
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
