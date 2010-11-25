package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.Classifier;
import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.model.Category;
import com.enigmastation.dao.model.Feature;
import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.impl.StemmingWordLister;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is a simple Bayesian calculation class. It was ported from Python
 * contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index
 * .html">Programming Collective Intelligence</a>," by Toby Segaran.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 36 $
 */
public class ClassifierImpl implements Classifier {
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    FeatureDAO featureDAO;
    /**
     *
     */
    @Autowired
    protected WordLister wordLister = null;

    @PostConstruct
    public synchronized void init() {
        if (wordLister == null) {
            wordLister = new StemmingWordLister();
        }
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
        Feature template = new Feature();
        template.setFeature(feature);
        template.setCategory(category);
        Feature f = featureDAO.read(template);
        if (f == null) {
            template.setCount(0L);
            f = template;
        } else {
            f.setCount(f.getCount() + 1);
        }
        featureDAO.write(f);
    }

    /**
     * Increase the count of a category. Direct port from Segaran's book,
     * including method name
     *
     * @param category the category to increment
     */
    void incc(String category) {
        Category template = new Category();
        template.setCategory(category);
        Category c = categoryDAO.read(template);
        if (c == null) {
            c = template;
            c.setCount(1L);
        } else {
            c.setCount(c.getCount() + 1);
        }
        categoryDAO.write(c);
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @param feature  the feature
     * @param category the category to query
     * @return the number of times a feature has appeared in a category
     */
    double fcount(String feature, String category) {
        Feature template = new Feature();
        template.setFeature(feature);
        template.setCategory(category);
        Feature f = featureDAO.read(template);
        if (f == null) {
            return 0.0;
        }
        return f.getCount();
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @param category the category to count items for
     * @return the number of items in a category
     */
    double catcount(String category) {
        Category template = new Category();
        template.setCategory(category);
        Category c = categoryDAO.read(template);
        if (c == null) {
            return 0.0;
        }
        return c.getCount();
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @return the total number of items
     */
    double totalcount() {
        Category template = new Category();
        double total = 0.0;
        for (Category c : categoryDAO.readMultiple(template)) {
            total += c.getCount();
        }
        return total;
    }

    /*double totalcount(String feature) {
        Map<String, Integer> classifierMap = this.classifierDataModelFactory
                .getFeatureMap(feature);
        if (classifierMap != null) {
            return getTotalCount(classifierMap);
        }
        return 0.0;
    }
    */
    double getTotalFeatureCount(String feature) {
        Feature template = new Feature();
        template.setFeature(feature);
        double total = 0.0;
        List<Feature> results = featureDAO.readMultiple(template);
        for (Feature f : results) {
            total += f.getCount();
        }
        return total;
    }

    /**
     * Direct port from Segaran's book, including method name.
     *
     * @return the list of all getCategories
     */
    public final Set<String> getCategories() {
        Category template = new Category();
        Set<String> results = new TreeSet<String>();
        for (Category c : categoryDAO.readMultiple(template)) {
            results.add(c.getCategory());
        }
        return Collections.unmodifiableSet(results);
    }

    public void train(Object item, String category) {
        Set<String> features = wordLister.getUniqueWords(item);

        for (String f : features) {
            incf(f, category);
        }
        incc(category);
    }

    /**
     * Convenience method for descendant classes - aids in porting from
     * Segaran's book.
     * <p/>
     * I want to change this method to use the arithmetic exception *only* if
     * it's rare. It's possible that determining rarity might be even more
     * expensive, though.
     *
     * @param feature  the feature to consider
     * @param category the category
     * @return the feature probability for the class
     */
    protected double fprob(String feature, String category) {
        try {
            return fcount(feature, category) / catcount(category);
        } catch (ArithmeticException ae) {
            return 0;
        }
    }

    /**
     * @param feature  the feature to consider
     * @param category the category
     * @return the feature probability for the class
     */
    public final double getFeatureProbability(String feature, String category) {
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
        double totals = getTotalFeatureCount(feature);
        return ((WEIGHT * ASSUMED_PROBABILITY) + (totals * basicprob))
                / (WEIGHT + totals);
    }

    public void setWordLister(WordLister wordLister) {
        this.wordLister=wordLister;
    }
}
