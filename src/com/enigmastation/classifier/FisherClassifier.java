package com.enigmastation.classifier;

public interface FisherClassifier extends NaiveClassifier {
    double getFisherProbability(String item, String category);

    void setMinimum(String category, double minimum);

    double getMinimum(String category);
}
