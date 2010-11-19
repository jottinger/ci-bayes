package com.enigmastation.classifier;

import java.text.MessageFormat;

public final class ClassifierProbability implements Comparable<ClassifierProbability> {
    private String category;
    private Double score;

    public String toString() {
        return MessageFormat.format("[ClassifierProbability:category=\"{0}\",score={1}]", category, score);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int compareTo(ClassifierProbability o) {
        if(getCategory().equals(o.getCategory())) {
            return 0;
        }
        
        return (int)(Math.signum(o.getScore()-getScore()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassifierProbability that = (ClassifierProbability) o;

        return category != null && category.equals(that.category) || score != null && score.equals(that.score);

    }

    @Override
    public int hashCode() {
        int result;
        result = (category != null ? category.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
