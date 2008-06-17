package com.enigmastation.classifier;

public interface FisherClassifier extends NaiveClassifier {
    @SuppressWarnings({"SameParameterValue"})
    double getFisherProbability(Object item, String category);

    void setMinimum(String category, double minimum);

    double getMinimum(String category);
}
