package com.enigmastation.classifier;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Apr 8, 2009
 * Time: 10:28:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Trainer extends Classifier {
    void train(Object item, String category);   
}
