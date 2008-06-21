package com.enigmastation.neuralnet.impl.neuralnets;

import com.enigmastation.neuralnet.NeuralNet;
import com.enigmastation.neuralnet.Resolver;

public class BaseNeuralNet<T> implements NeuralNet<T> {
    Resolver<T> resolver;
    public BaseNeuralNet(Resolver<T> resolver) {
        this.resolver=resolver;        
    }

    public void generateHiddenNode(T[] origins, T[] destinations) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
