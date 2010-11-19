package com.enigmastation.classifier.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.enigmastation.classifier.ClassificationListener;
import com.enigmastation.classifier.ClassifierException;
import com.enigmastation.classifier.ClassifierProbability;
import com.enigmastation.classifier.NaiveClassifier;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

/**
 * This is a naive bayesian classifier.
 * It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 38 $
 */
public class NaiveClassifierImpl extends ClassifierImpl implements NaiveClassifier {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3694360766223901139L;
	private final transient Map<String, Double> thresholds = new MapMaker().makeMap();

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
        Set<String> features = wordLister.getUniqueWords(item);
        if (classificationListeners.size() > 0) {
            for (ClassificationListener listener : classificationListeners) {
                listener.onClassification(this, features);
            }
        }

        int index = 0;
        for (String cat : getCategories()) {
            probabilities[index] = new ClassifierProbability();
            probabilities[index].setCategory(cat);
            probabilities[index].setScore(getProbabilityForCategory(features, cat));
            index++;
        }
        Arrays.sort(probabilities);
        return probabilities;
    }

    public void normalizeProbabilities(ClassifierProbability[] probabilities) {
        if (probabilities.length > 0) {
            double multiplier = 1.0 / probabilities[0].getScore();
            for (ClassifierProbability prob : probabilities) {
                prob.setScore(prob.getScore() * multiplier);
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

    /**
     * This is an implementation that ignores thresholds. If there is no data in the classifier,
     * whooey! You get an exception. This resolves issue #2.
     *
     * @param item the item to be classified; needs to have meaningful toString()
     * @return a meaningful classification.
     */
    public String getClassification(Object item) {
        if (getCategories().size() == 0) {
            throw new ClassifierException("No categories; please train before classification"
                    + " or provide default category.");
        }
        ClassifierProbability[] probs = getProbabilities(item);
        ClassifierProbability cp = probs[0];

        for (ClassifierProbability p : probs) {
            if (p.getCategory().equals(cp.getCategory())) {
                continue;
            }
            if (p.getScore() > cp.getScore()) {
                cp = p;
            }
        }
        return cp.getCategory();
    }

    protected double docprob(String item, String category) {
        return getDocumentProbabilityForCategory(item, category);
    }

    public double getDocumentProbabilityForCategory(Object item, String category) {
        Set<String> features = wordLister.getUniqueWords(item);
        return getDocumentProbabilityForCategory(features, category);
    }

    public double getDocumentProbabilityForCategory(Set<String> features, String category) {
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

    protected Set<ClassificationListener> classificationListeners = Sets.newHashSet();

    public void addListener(ClassificationListener listener) {
        classificationListeners.add(listener);
    }


}
