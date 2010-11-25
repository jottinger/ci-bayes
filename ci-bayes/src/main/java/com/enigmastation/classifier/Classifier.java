package com.enigmastation.classifier;

import java.io.Serializable;
import java.util.Set;

/**
 * This is the basic classifier interface.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 36 $
 * @see com.enigmastation.classifier.impl.ClassifierImpl
 */
public interface Classifier extends Serializable {
    double getFeatureProbability(String feature, String category);

    double getWeightedProbability(String feature, String category);

    //Map<String, Map<String, Integer>> getCategoryFeatureMap();

    //Map<String,Integer> getCategoryDocCount();

    Set<String> getCategories();

    void train(Object item, String category);
}
