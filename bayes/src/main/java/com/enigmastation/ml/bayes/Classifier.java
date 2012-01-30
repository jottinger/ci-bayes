package com.enigmastation.ml.bayes;

import java.util.Map;

public interface Classifier {
  Object classify(Object source);
  Object classify(Object source, Object defaultClassification);
  Object classify(Object source, Object defaultClassification, double strength);
  Map<Object, Double> getClassificationProbabilities(Object source);
  void train(Object source, Object classification);
}
