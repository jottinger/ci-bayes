package com.enigmastation.classifier;

import java.util.Set;

/**
 * This is a basic word filter - it exists solely to take a string and pass back a list of tokens.
 * @see com.enigmastation.classifier.impl.SimpleWordLister
 * @version $Revision: 36 $
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 */
public interface WordLister {
    Set<String> getUniqueWords(Object document);
}
