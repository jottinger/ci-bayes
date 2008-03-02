package com.enigmastation.classifier;

import gnu.trove.TObjectIntHashMap;

/**
 * @version: $Revision$
 */
public final class ClassifierMap extends TObjectIntHashMap<String> {
    private long totalCount;

    public void incrementCategory(String category) {
        //       int i=0;
//        try {
//            i=get(category)+1;
        //       } catch(NullPointerException npe) {
        //          i=1;
        //     }
        if (this.containsKey(category)) {
            this.increment(category);
        } else {
            put(category, 1);
        }
//        put(category, i);
        totalCount += 1;
    }

    public double getTotalCount() {
        return totalCount;
    }
}
