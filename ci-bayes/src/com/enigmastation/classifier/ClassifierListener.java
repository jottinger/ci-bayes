package com.enigmastation.classifier;

public interface ClassifierListener {
    void handleFeatureUpdate(FeatureIncrement fi);

    void handleCategoryUpdate(CategoryIncrement fi);
}
