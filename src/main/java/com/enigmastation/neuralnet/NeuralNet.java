package com.enigmastation.neuralnet;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jun 21, 2008
 * Time: 10:46:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface NeuralNet<T> {
    void generateHiddenNode(T[] origins, T[] destinations);
}
