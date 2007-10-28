package com.enigmastation.classifier;

import java.util.Map;
import java.util.Set;

/**
 * This is the basic classifier interface.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision$
 * @see com.enigmastation.classifier.impl.ClassifierImpl
 */
public interface Classifier {
    void train(String item, String category);

    double getFeatureProbability(String feature, String category);

    double getWeightedProbability(String feature, String category);

    Map<String, Map<String, Integer>> getCategoryFeatureMap();

    Map<String, Integer> getCategoryDocCount();

    Set<String> getCategories();
}
