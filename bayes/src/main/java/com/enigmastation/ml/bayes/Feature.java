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
        Integer count = categories.get(category);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public void incrementCategoryCount(Object category) {
        Integer oldCount = categories.get(category);
        if (oldCount == null) {
            oldCount = 0;
        }
        categories.put(category, oldCount + 1);
    }
}
