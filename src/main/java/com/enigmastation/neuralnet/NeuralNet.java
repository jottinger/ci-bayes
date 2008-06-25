package com.enigmastation.neuralnet;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jun 21, 2008
 * Time: 10:46:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface NeuralNet {
    void generateHiddenNode(String[] origins, String[] destinations);

    List<Double> getResult(String[] strings, String[] strings1);

    void trainquery(String[] strings, String[] strings1, String result);
}
