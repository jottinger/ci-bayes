package com.enigmastation.extractors;

import java.util.Set;

/**
 * This is a basic word filter - it exists solely to take a string and pass back a list of tokens.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 36 $
 * @see com.enigmastation.extractors.impl.SimpleWordLister
 */
public interface WordLister {
    Set<String> getUniqueWords(Object document);
}