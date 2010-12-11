package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.model.Neuron;
import com.gigaspaces.simpledao.dao.Pair;

import java.util.List;

public interface NeuralNetwork {
    void trainNaive(String inputs, String output);
    public void train(String corpus, String result);

    List<Pair<Neuron, Double>> getOutputs(String corpus);
}
