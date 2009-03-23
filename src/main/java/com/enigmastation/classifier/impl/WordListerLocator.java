package com.enigmastation.classifier.impl;

import com.enigmastation.extractors.WordLister;


/**
 * General interface for locatoring the default WordLister implementation.
 * Only reason for this seemingly pointless interface is to allow ClassifierImpl to
 * remain jdk5 compatable.
 */
public interface WordListerLocator {
  WordLister locate();
}
