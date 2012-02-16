package com.enigmastation.ml.bayes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Feature implements Serializable {
    Object feature;
    Map<Object, Integer> categories = new HashMap<>();

    public Map<Object, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<Object, Integer> categories) {
        this.categories = categories;
    }

    public Object getFeature() {
        return feature;
    }

    public void setFeature(Object feature) {
        this.feature = feature;
    }

    public Integer getCountForCategory(Object category) {
        return categories.get(category);
    }

    public void incrementCategoryCount(Object category) {
        categories.put(category, categories.get(category) + 1);
    }
}
