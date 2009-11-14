package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.*;
import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.WordListerFactory;
import com.enigmastation.extractors.impl.StemmingWordListerFactory;
import com.enigmastation.extractors.impl.StemmingWordLister;
import javolution.util.FastSet;

import java.util.Collections;
import java.util.Set;
import java.util.Map;

/**
 * This is a simple Bayesian calculation class. It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 36 $
 */
public class ClassifierImpl implements Classifier {

    protected WordLister wordLister = null;
    private Set<ClassifierListener> trainingListeners;
    private ClassifierDataModelFactory classifierDataModelFactory;
    private boolean initialized = false;

    public synchronized void init()
    {
        if (!initialized)
        {
            if (getClassifierDataModelFactory() == null)
            {
                setClassifierDataModelFactory(new BasicClassifierDataModelFactory());
            }

            if(wordLister == null)
            {
                wordLister = new StemmingWordLister();
            }

            initialized = true;
        }
    }

    public ClassifierDataModelFactory getClassifierDataModelFactory() {
        return classifierDataModelFactory;
    }

    public void setClassifierDataModelFactory(ClassifierDataModelFactory classifierDataModelFactory) {
        if (getClassifierDataModelFactory() != null) {
            throw new IllegalStateException("Cannot set ModelFactory twice; old type is "
                    + getClassifierDataModelFactory().getClass().getName());
        }
        this.classifierDataModelFactory = classifierDataModelFactory;
    }

    public void addListener(ClassifierListener listener)
    {
        if(trainingListeners == null)
            trainingListeners = new FastSet<ClassifierListener>();
        trainingListeners.add(listener);
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

        //Map<String,Map<String,Integer>> featureMap = this.categoryFeatureMap;
        //Map<String,Integer> fm = featureMap.get(feature);
        Map<String,Integer> fm = this.classifierDataModelFactory.getFeatureMap(feature);
        if(fm == null)
        {
            //throw new IllegalStateException("You must either supply all classifiers in a map or supply a factory");
            throw new IllegalStateException("You must be able to create a feature map");
        }

        incrementCategory(fm, category);

        FeatureIncrement fi = null;
        if(trainingListeners != null)
        {
            for (ClassifierListener l : trainingListeners) {
                if (fi == null) {
                    fi = new FeatureIncrement(feature, category, fm.get(category));
                }
                l.handleFeatureUpdate(fi);
            }
        }

    }

    /**
     * Increase the count of a category.
     * Direct port from Segaran's book, including method name
     *
     * @param category the category to increment
     */
    void incc(String category) {

        incrementCategory(getCategoryDocCount(), category);

        CategoryIncrement ci = null;
        if(trainingListeners != null)
        {
            for (ClassifierListener l : trainingListeners) {
                if (ci == null) {
                    ci = new CategoryIncrement(category, getCategoryDocCount().get(category));
                    ci.setCountDelta(1);
                }
                l.handleCategoryUpdate(ci);
            }
        }

    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @param feature  the feature
     * @param category the category to query
     * @return the number of times a feature has appeared in a category
     */
    double fcount(String feature, String category)
    {
        Map<String,Integer> fm = this.classifierDataModelFactory.getFeatureMap(feature);
        if(fm != null)
        {
            Integer count = fm.get(category);
            if(count != null)
            {
                return count;
            }
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
        return getCategoryDocCount().get(category);
    }

    /**
     * Direct port from Segaran's book, including method name
     *
     * @return the total number of items
     */
    double totalcount() {
        Map<String,Integer> map = getCategoryDocCount();
        return getTotalCount(map);
    }

    double totalcount(String feature)
    {
        Map<String,Integer> classifierMap = this.classifierDataModelFactory.getFeatureMap(feature);
        if (classifierMap != null)
        {
            return getTotalCount(classifierMap);
        }
        return 0.0;
    }

    double getTotalFeatureCount(String feature) {
        Map<String,Integer> classifierMap = this.classifierDataModelFactory.getFeatureMap(feature);
        if (classifierMap != null)
        {
            return getTotalCount(classifierMap);
        }
        return 0.0;
    }

    /**
     * Direct port from Segaran's book, including method name.
     *
     * @return the list of all getCategories
     */
    public final Set<String> getCategories() {
        return Collections.unmodifiableSet(getCategoryDocCount().keySet());
    }

    public void train(Object item, String category) {
        Set<String> features = wordLister.getUniqueWords(item);

        for (String f : features) {
            incf(f, category);
        }
        incc(category);
    }

    /**
     * Convenience method for descendant classes - aids in porting from Segaran's book.
     * <p/>
     * I want to change this method to use the arithmetic exception *only* if it's rare. It's possible
     * that determining rarity might be even more expensive, though.
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
        return ((WEIGHT * ASSUMED_PROBABILITY) + (totals * basicprob)) / (WEIGHT + totals);
    }

    public final Map<String,Integer> getCategoryDocCount()
    {
        return classifierDataModelFactory.getCategoryCountMap();
    }

    private double getTotalCount(Map<String, Integer> map)
    {
        int totalCount = 0;
        for(Integer i:map.values()) {
            totalCount+=i;
        }
        return totalCount;
    }

    private void incrementCategory(Map<String,Integer> map, String category)
    {
        Integer val = map.get(category);
        if (val != null)
        {
            map.put(category, val+1);
        }
        else
        {
            map.put(category, 1);
        }
    }
}
