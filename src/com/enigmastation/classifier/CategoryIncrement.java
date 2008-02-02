package com.enigmastation.classifier;


public class CategoryIncrement {
    String category;
    int count;

    public final String getCategory() {
        return category;
    }

    public final void setCategory(String category) {
        this.category = category;
    }

    public final int getCount() {
        return count;
    }

    public final void setCount(int count) {
        this.count = count;
    }

    public CategoryIncrement(String category, Integer integer) {
        this.category=category;
        this.count=integer;
    }

    public String toString() {
        return new StringBuilder().append("CategoryIncrement[category=")
                .append(category)
                .append(",count=")
                .append(count)
                .append("]").toString();
    }
}
