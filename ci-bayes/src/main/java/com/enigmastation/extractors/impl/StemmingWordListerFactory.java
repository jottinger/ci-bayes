package com.enigmastation.extractors.impl;

import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.WordListerFactory;

public class StemmingWordListerFactory implements WordListerFactory {
    public WordLister build() {
        return new StemmingWordLister();
    }
}
