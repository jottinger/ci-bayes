package com.enigmastation.neuralnet;

import java.util.List;

/**
 * This is the basic NeuralNet interface. It provides everything you should need to
 * use a neural network (as implemented in this project, at least!), given a valid implementation.
 * <p/>
 * <p>The way it's designed to work is fairly simple: you train it with a set of
 * origins and a set of destinations, as well as the result that was chosen (which
 * strengthens the nodes referenced.)
 * <p/>
 * <p>Then, to get results, you pass in the set of origins and destinations (again), and
 * it will return a list containing some floating point values. The strongest of those is
 * the "neural network return value."
 * <p/>
 * <p><strong>This API kinda sucks and will change to be more user-friendly. Soon.</strong>
 * <p>Ideally, a new interface will be created that doesn't require the whole set of destinations
 * to be included in the training set... I think.
 */
public interface NeuralNet {
    List<Double> getResult(String[] origins, String[] destinations);

    void trainquery(String[] origins, String[] destinations, String result);
}
