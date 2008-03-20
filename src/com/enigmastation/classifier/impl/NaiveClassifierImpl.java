package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.ClassifierProbability;
import com.enigmastation.classifier.NaiveClassifier;
import com.enigmastation.classifier.WordLister;
import javolution.util.FastMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * This is a naive bayesian classifier.
 * It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision$
 */
public class NaiveClassifierImpl extends ClassifierImpl implements NaiveClassifier {
    private final transient Map<String, Double> thresholds = new FastMap<String, Double>();

    public NaiveClassifierImpl(WordLister w) {
        super(w);
    }

    public NaiveClassifierImpl() {
        super();
    }

    public void setCategoryThreshold(String cat, double t) {
        thresholds.put(cat, t);
    }

    public double getCategoryThreshold(String cat) {
        if (thresholds.containsKey(cat)) {
            return thresholds.get(cat);
        } else {
            return 1.0;
        }
    }

    protected String classify(String item, String defaultCat) {
        return getClassification(item, defaultCat);
    }

    public ClassifierProbability[] getProbabilities(final Object item) {
        ClassifierProbability[] probabilities = new ClassifierProbability[getCategories().size()];
        int index = 0;
        for (String cat : getCategories()) {
            probabilities[index] = new ClassifierProbability();
            probabilities[index].setCategory(cat);
            probabilities[index].setScore(getProbabilityForCategory(item, cat));
            index++;
        }
        Arrays.sort(probabilities);
        return probabilities;
    }

    public void normalizeProbabilities(ClassifierProbability[] probabilities) {
        if(probabilities.length>0) {
            double multiplier=1.0/probabilities[0].getScore();
            for(ClassifierProbability prob:probabilities) {
                prob.setScore(prob.getScore()*multiplier);
            }
        }
    }

    public String getClassification(Object item, String defaultCat) {
        if (getCategories().size() == 0) {
            return defaultCat;
        }

        ClassifierProbability[] probs = getProbabilities(item);
        ClassifierProbability cp = probs[0];

        for (ClassifierProbability p : probs) {
            if (p.getCategory().equals(cp.getCategory())) {
                continue;
            }
            if (p.getScore() * getCategoryThreshold(cp.getCategory()) > cp.getScore()) {
                return defaultCat;
            }
        }
        return cp.getCategory();
    }

    /*
        private String getClassificationOld(Object item, String defaultCat) {
            Map<String, Double> probs = new FastMap<String, Double>();

            double max = 0.0;
            String category = null;
            for (String cat : getCategories()) {
                double p = getProbabilityForCategory(item, cat);
                probs.put(cat, p);
                if (p > max) {
                    max = p;
                    category = cat;
                }
            }

            for (String cat : probs.keySet()) {
                if (cat.equals(category))
                    continue;
                if (probs.get(cat) * getCategoryThreshold(category) > probs.get(category)) {
                    return defaultCat;
                }
            }
            return category;
        }
    */
    protected double docprob(String item, String category) {
        return getDocumentProbabilityForCategory(item, category);
    }

    public double getDocumentProbabilityForCategory(Object item, String category) {
        Set<String> features = this.extractor.getUniqueWords(item);
        double p = 1.0;
        for (String f : features) {
            p *= getWeightedProbability(f, category);
        }
        return p;
    }

    protected double prob(String item, String category) {
        return getProbabilityForCategory(item, category);
    }

    public double getProbabilityForCategory(Object item, String category) {
        double catprob = catcount(category);
        catprob /= totalcount();
        double dp = getDocumentProbabilityForCategory(item, category);
        dp *= catprob;
        return dp;
    }
}
