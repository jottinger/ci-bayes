package com.enigmastation.classifier;

/**
 * This represents a change record for a category. It primarily exists so that serialization efforts
 * can be notified of changes in the underlying category map. This is managed as part of the ClassifierImpl
 * base class, and events of this type are created as a side-effect of training a Classifier.
 */
public class CategoryIncrement {
    String category;
    int count;

    /**
     * Returns the category for the given increment event.
     * @return the category for the increment event
     */
    public final String getCategory() {
        return category;
    }

    /**
     * Sets the category for a given increment event.
     * @param category The new category for the increment event
     */
    public final void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the new count for the category in the event
     * @return the new count value for the category in the event
     */
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
