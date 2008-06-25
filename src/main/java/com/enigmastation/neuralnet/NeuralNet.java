package com.enigmastation.neuralnet;

import java.util.List;

public interface NeuralNet {
    List<Double> getResult(String[] strings, String[] strings1);

    void trainquery(String[] strings, String[] strings1, String result);
}
