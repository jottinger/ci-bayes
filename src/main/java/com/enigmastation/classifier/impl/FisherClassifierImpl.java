package com.enigmastation.classifier.impl;

import java.util.Map;
import java.util.Set;

import com.enigmastation.classifier.ClassifierProbability;
import com.enigmastation.classifier.FisherClassifier;
import com.google.common.collect.MapMaker;

public class FisherClassifierImpl extends NaiveClassifierImpl implements FisherClassifier {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5492366632949643784L;
	private Map<String, Double> minimums = new MapMaker().makeMap();

    public void setMinimum(String category, double minimum) {
        minimums.put(category, minimum);
    }

    public double getMinimum(String category) {
        if (minimums.containsKey(category)) {
            return minimums.get(category);
        }
        return 0.0;
    }

    @Override
    protected double fprob(String feature, String cat) {
        double clf = super.fprob(feature, cat);
        if (clf == 0.0) {
            return 0.0;
        }
        double freqsum = 0.0;

        for (String c : getCategories()) {
            double d = super.fprob(feature, c);
            freqsum = freqsum + d;
        }

        return clf / freqsum;
    }

    double fisherprob(Object item, String cat) {
        double p = 1.0;
        Set<String> features = wordLister.getUniqueWords(item);
        for (String f : features) {
            p *= getWeightedProbability(f, cat);
        }
        double fscore = -2 * Math.log(p);
        return invchi2(fscore, features.size() * 2);
    }

    public double getFisherProbability(Object item, String category) {
        return fisherprob(item, category);
    }

    public double getProbabilityForCategory(Object item, String category) {
        return fisherprob(item, category);
    }

    private double invchi2(double chi, int df) {
        double m = chi / 2.0;
        double sum = Math.exp(-m);
        double term = sum;
        for (int i = 1; i < df / 2; i++) {
            term *= m / i;
            sum += term;
        }
        return Math.min(sum, 1.0);
    }

    /*
    public String getClassificationOld(Object item, String defaultCat) {
        String best = defaultCat;
        double max = 0.0;
        for (String c : getCategories()) {
            double p = fisherprob(item, c);
            if (p > getMinimum(c) && p > max) {
                best = c;
                max = p;
            }
        }
        return best;
    }
    */

    @Override
    public String getClassification(Object item, String defaultCat) {
        if (getCategories().size() == 0) {
            return defaultCat;
        }
        ClassifierProbability[] probs = getProbabilities(item);
        for (ClassifierProbability p : probs) {
            if (p.getScore() > getMinimum(p.getCategory())) {
                return p.getCategory();
            }
        }
        return defaultCat;
    }
}
