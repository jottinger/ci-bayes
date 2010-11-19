package com.enigmastation.classifier;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Apr 8, 2009
 * Time: 10:28:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClassificationListener {
    void onClassification(Classifier c, Set<String> words);
}
