package com.enigmastation.classifier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class serves as the basic container for the classifiers. It's based currently on ConcurrentHashMap.
 * Note that this class tends to change underlying implementations from commit to commit because various
 * implementations work better than others; for example, right now it's a hashmap, and it's entirely likely
 * that a treemap (or, perhaps, a trie) would be better. Further tests are required.
 *
 * <p>Relevant data will include memory usage and speed over the entire training corpus.
 *
 * <p>This class stores the number of hits for a given category, so if you train five times in category
 * "X", the map will contain "5" as  the value for the key "X".
 * 
 * @author <a href="mailto:joeo@enigmastation.com">Joseph Ottinger</a>
 * @version $Revision: 36 $
 */

public class ClassifierMap extends ConcurrentHashMap<String,Integer> {
    private long totalCount=0;

    /**
     * This method increments the category's count. It primarily exists for the benefit of bulk loaders,
     * which might not want to increment by single values, 400 times.
     * 
     * @param category The category for which the value changes
     * @param amount The delta for the change
     */
    @SuppressWarnings({"SameParameterValue"})
    public void incrementCategory(String category, int amount) {
        if (containsKey(category)) {
            put(category,get(category)+amount);
        } else {
            put(category, amount);
        }

        totalCount += amount;
    }

    /**
     * This method increments the "hits" for a given category by one. This is the 'normal' method that
     * a training process will call, one time for each category/corpus combination.
     * 
     * @param category The category for which the value changes
     */
    public void incrementCategory(String category) {
        incrementCategory(category, 1);
    }

    /**
     * This returns the total number of category corpora. If you have a map that looks like this:
     * ["X":5, "Y":3], this should return 8 - the total of the values for every map entry.
     *
     * @return The total number of training corpuses
     */
    public double getTotalCount() {
    if(totalCount==0) {
    for(Integer i:values()) {
    totalCount+=i;
    }
    }
        return totalCount;
    }
}
