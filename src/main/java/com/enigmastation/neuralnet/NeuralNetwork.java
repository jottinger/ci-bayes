package com.enigmastation.neuralnet;

import java.util.Map;

public interface NeuralNetwork {
    void train(String conclusion, String corpus);

    Map<String, Double> analyze(String categories, String corpus);
}
