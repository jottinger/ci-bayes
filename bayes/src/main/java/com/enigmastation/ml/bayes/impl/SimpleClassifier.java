package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.Classifier;
import com.enigmastation.ml.bayes.Tokenizer;
import com.enigmastation.ml.common.collections.MapBuilder;
import com.enigmastation.ml.common.collections.ValueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleClassifier implements Classifier {
  Map<Object, Map<Object, Integer>> features;
  Map<Object, Integer> categories;
  Tokenizer tokenizer = new PorterTokenizer();
  Map<Object, Double> thresholds = new MapBuilder().defaultValue(1.0).build();

  SimpleClassifier(Map<Object, Map<Object, Integer>> features, Map<Object, Integer> categories) {
    this.features = features;
    this.categories = categories;
  }

  public SimpleClassifier() {
    features = new MapBuilder().valueProvider(new ValueProvider<Object, Object>() {
      @Override
      public Object getDefault(Object k) {
        return new MapBuilder().defaultValue(0).build();
      }
    }).build();
    categories = new MapBuilder().defaultValue(0).build();
  }

  @Override
  public Object classify(Object source) {
    return classify(source, "none");
  }

  @Override
  public Object classify(Object source, Object defaultClassification) {
    return classify(source, defaultClassification, 0.0);
  }

  @Override
  public Object classify(Object source, Object defaultClassification, double strength) {
    Map<Object, Double> probs = getClassificationProbabilities(source);
    double max = 0.0;
    Object category = null;
    for (Map.Entry<Object, Double> entry : probs.entrySet()) {
      if (entry.getValue() > max) {
        max = entry.getValue();
        category = entry.getKey();
      }
    }
    for (Map.Entry<Object, Double> entry : probs.entrySet()) {
      if (entry.getKey().equals(category)) {
        continue;
      }

      if ((entry.getValue() * getThreshold(category)) > probs.get(category)) {
        return defaultClassification;
      }
    }
    return category;
  }

  @Override
  public Map<Object, Double> getClassificationProbabilities(Object source) {
    Map<Object, Double> probabilities = new HashMap<Object, Double>();
    for (Object category : categories()) {
      probabilities.put(category, docprob(source, category));
    }
    return probabilities;
  }

  @Override
  public void train(Object source, Object classification) {
    for (Object feature : tokenizer.tokenize(source)) {
      incf(feature, classification);
    }
    incc(classification);
  }

  private void incf(Object feature, Object category) {
    Map<Object, Integer> cat = features.get(feature);
    features.put(feature, cat);
    cat.put(category, cat.get(category) + 1);
  }

  private void incc(Object category) {
    categories.put(category, categories.get(category) + 1);
  }

  // the number of times a feature has occurred in a category
  int fcount(Object feature, Object category) {
    if (features.containsKey(feature) && features.get(feature).containsKey(category)) {
      return features.get(feature).get(category);
    }
    return 0;
  }

  int catcount(Object category) {
    if (categories.containsKey(category)) {
      return categories.get(category);
    }
    return 0;
  }

  int totalcount() {
    int sum = 0;
    for (Integer i : categories.values()) {
      sum += i;
    }
    return sum;
  }

  Set<Object> categories() {
    return categories.keySet();
  }

  double fprob(Object feature, Object category) {
    if (catcount(category) == 0) {
      return 0.0;
    }
    return (1.0 * fcount(feature, category)) / catcount(category);
  }

  double weightedprob(Object feature, Object category, double weight, double assumedProbability) {
    double basicProbability = fprob(feature, category);
    //System.out.println("basic probability: "+basicProbability);
    double totals = 0;
    for (Object cat : categories()) {
      totals += fcount(feature, cat);
    }
    //System.out.printf("((%f * %f)+(%f * %f))/(%f + %f) %n", weight,
    //assumedProbability, totals, basicProbability, weight, totals);
    return ((weight * assumedProbability) + (totals * basicProbability)) / (weight + totals);
  }

  double weightedprob(Object feature, Object category, double weight) {
    return weightedprob(feature, category, weight, 0.5);
  }

  double weightedprob(Object feature, Object category) {
    return weightedprob(feature, category, 1.0);
  }

  /* naive bayes, very naive - and not what we usually need. */
  double docprob(Object corpus, Object category) {
    double p = 1.0;
    for (Object f : tokenizer.tokenize(corpus)) {
      p *= weightedprob(f, category);
    }
    return p;
  }

  double prob(Object corpus, Object category) {
    double catprob = (1.0 * catcount(category)) / totalcount();
    double docprob = docprob(corpus, category);
    return docprob * catprob;
  }

  public void setThreshold(Object category, Double threshold) {
    thresholds.put(category, threshold);
  }

  public double getThreshold(Object category) {
    return thresholds.get(category);
  }


}
