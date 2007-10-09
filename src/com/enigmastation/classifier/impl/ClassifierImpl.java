package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.Classifier;
import com.enigmastation.classifier.WordLister;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a simple Bayesian calculation class. It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision$
 */
public class ClassifierImpl implements Classifier {
    /**
     * In Segaran's book, this is referred to as "fc"
     */
    private Map<String, Map<String, Integer>> categoryFeatureMap = new FastMap<String, Map<String, Integer>>();
    /**
     * In Segaran's book, this is referred to as "cc"
     */
    private Map<String, Integer> categoryDocCount = new FastMap<String, Integer>();
    WordLister extractor = null;


    public ClassifierImpl(WordLister w) {
        extractor = w;
    }

    public ClassifierImpl() {
        this(new WordListerImpl());
    }

    /**
     * Increase the count of a feature/category pair.
     * <p/>
     * Direct port from Segaran's book, including method name
     *
     * @param feature  the feature (the 'word')
     * @param category the category
     */
    void incf(String feature, String category) {
        Map<String, Integer> fm = getCategoryFeatureMap().get(feature);
        if (fm == null) {
            getCategoryFeatureMap().put(feature, new HashMap<String, Integer>());
            fm = getCategoryFeatureMap().get(feature);
        }
        Integer c = 1;
        if (fm.containsKey(category)) {
            c = fm.get(category) + 1;
        }
        fm.put(category, c);
    }

    /**
     * Increase the count of a category.
     * Direct port from Segaran's book, including method name
     *
     * @param category the category to increment
     */
    void incc(String category) {
        int i = 1;
        if (getCategoryDocCount().containsKey(category)) {
            i = getCategoryDocCount().get(category) + 1;
        }
        getCategoryDocCount().put(category, i);
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @param feature  the feature
     * @param category the category to query
     * @return the number of times a feature has appeared in a category
     */
    double fcount(String feature, String category) {
        if (getCategoryFeatureMap().containsKey(feature) && getCategoryFeatureMap().get(feature).containsKey(category)) {
            return getCategoryFeatureMap().get(feature).get(category);
        }
        return 0.0;
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @param category the category to count items for
     * @return the number of items in a category
     */
    double catcount(String category) {
        if (getCategoryDocCount().containsKey(category)) {
            return getCategoryDocCount().get(category);
        }
        return 0.0;
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @return the total number of items
     */
    double totalcount() {
        long d = 0;
        for (Integer i : getCategoryDocCount().values()) {
            d += i;
        }
        return d;
    }

    /**
     * Direct port from Segaran's book, including method name.
     *
     * @return the list of all categories
     */
    Set<String> categories() {
        Set<String> cats = new FastSet<String>();
        cats.addAll(getCategoryDocCount().keySet());
        return cats;
    }

    public void train(String item, String category) {
        Set<String> features = extractor.getUniqueWords(item);

        for (String f : features) {
            incf(f, category);
        }
        incc(category);
    }

    /**
     * Convenience method for descendant classes - aids in porting from Segaran's book.
     *
     * @param feature  the feature to consider
     * @param category the category
     * @return the feature probability for the class
     */
    protected double fprob(String feature, String category) {
        if (catcount(category) == 0) {
            return 0;
        }

        return fcount(feature, category) / catcount(category);
    }

    /**
     * @param feature  the feature to consider
     * @param category the category
     * @return the feature probability for the class
     */
    public double getFeatureProbability(String feature, String category) {
        return fprob(feature, category);
    }


    private double WEIGHT = 1.0;
    private double ASSUMED_PROBABILITY = 0.5;

    protected double weightedprob(String feature, String category) {
        return getWeightedProbability(feature, category);
    }

    /**
     * @param feature  The feature to consider
     * @param category the category to consider weight for
     * @return the weighted probability
     */
    public double getWeightedProbability(String feature, String category) {
        double basicprob = getFeatureProbability(feature, category);
        long totals = 0;
        for (String cat : categories()) {
            totals += fcount(feature, cat);
        }
        return ((WEIGHT * ASSUMED_PROBABILITY) + (totals * basicprob)) / (WEIGHT + totals);
    }

    public Map<String, Map<String, Integer>> getCategoryFeatureMap() {
        return categoryFeatureMap;
    }

    public Map<String, Integer> getCategoryDocCount() {
        return categoryDocCount;
    }
}
