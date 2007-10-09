package com.enigmastation.classifier;

import java.util.Map;

/**
 * This is the basic classifier interface.
 * @see com.enigmastation.classifier.impl.ClassifierImpl
 * @version $Revision$
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 */
public interface Classifier {
    void train(String item, String category);

    double getFeatureProbability(String feature, String category);

    double getWeightedProbability(String feature, String category);

    Map<String, Map<String, Integer>> getCategoryFeatureMap();

    Map<String, Integer> getCategoryDocCount();
}
