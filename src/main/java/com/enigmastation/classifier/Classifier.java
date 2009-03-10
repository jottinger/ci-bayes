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
    void train(Object item, String category);

    double getFeatureProbability(String feature, String category);

    double getWeightedProbability(String feature, String category);

    FeatureMap getCategoryFeatureMap();

    ClassifierMap getCategoryDocCount();

    Set<String> getCategories();

    /**
     * Convenience method for loaders. Don't use this any more; it shouldn't be necessary.
     *
     * @param category category to create explicitly
     * @deprecated since 1.0.6
     */
    public void addCategory(String category);

    void addListener(ClassifierListener listener);

    FeatureMap createFeatureMap();

    ClassifierMap createClassifierMap();
}
