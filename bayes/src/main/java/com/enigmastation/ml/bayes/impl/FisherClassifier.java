package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.common.collections.MapBuilder;

import java.util.List;
import java.util.Map;

public class FisherClassifier extends SimpleClassifier {
    Map<Object, Double> minimums = new MapBuilder().defaultValue(0.0).build();

    public double getMinimum(Object category) {
        return minimums.get(category);
    }

    public void setMinimum(Object category, double v) {
        minimums.put(category, v);
    }

    public FisherClassifier() {
    }

    public FisherClassifier(Map<Object, Map<Object, Integer>> features, Map<Object, Integer> categories) {
        super(features, categories);
    }

    double fprob(Object feature, Object category) {
        double clf = super.fprob(feature, category);
        if (clf == 0.0) {
            return 0.0;
        }
        double freqsum = 0.0;
        for (Object c : categories()) {
            freqsum += super.fprob(feature, c);
        }
        return clf / freqsum;
    }

    double fisherprob(Object source, Object category) {
        double p = 1.0;
        List<Object> features = getFeatures(source);
        for (Object f : features) {
            p *= weightedprob(f, category);
        }
        double fscore = -2.0 * Math.log(p);
        return invchi(fscore, features.size() * 2);
    }

    protected double invchi(double chi, double df) {
        double m = chi / 2.0;
        double sum = Math.exp(-m);
        double term = sum;
        for (int i = 1; i < df / 2; i++) {
            term *= (m / i);
            sum += term;
        }
        return Math.min(sum, 1.0);
    }

    @Override
    public Object classify(Object source, Object defaultClassification) {
        Object best = defaultClassification;
        double max = 0.0;
        for (Object c : categories()) {
            double p = fisherprob(source, c);
            if (p > getMinimum(c) && p > max) {
                best = c;
                max = p;
            }
        }
        return best;
    }
}
