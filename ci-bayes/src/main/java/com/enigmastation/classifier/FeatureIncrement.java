package com.enigmastation.classifier;

public class FeatureIncrement extends CategoryIncrement {
    private String feature;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


    public FeatureIncrement(String feature, String category, Integer integer) {
        super(category, integer);
        this.feature=feature;
    }

    public String toString() {
        return new StringBuilder().append("FeatureIncrement[feature=")
                .append(feature)
                .append(",category=")
                .append(category)
                .append(",count=")
                .append(count)
                .append("]").toString();
    }
}
