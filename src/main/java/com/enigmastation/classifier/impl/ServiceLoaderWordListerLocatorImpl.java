package com.enigmastation.classifier.impl;

import com.enigmastation.extractors.WordLister;

import java.util.ServiceLoader;

/**
 * Implementation of WordListerLocator that uses the JDK6 ServiceLoader API.
 */
public class ServiceLoaderWordListerLocatorImpl implements WordListerLocator {

  public WordLister locate() {
    ServiceLoader<WordLister> wordListerLoader = ServiceLoader.load(WordLister.class);
    return wordListerLoader.iterator().next();
  }
}
