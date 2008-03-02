/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enigmastation.collections;

/**
 *
 * @author jottinger
 */
public class TrieDouble {

    static final int BRANCHES = 256;
    final boolean validateKey;
    double value;

    double value() {
        return value;
    }
    
    final TrieDouble[] branches = new TrieDouble[BRANCHES];

    public TrieDouble(boolean limitCharSet) {
        validateKey=limitCharSet;
    }

    public TrieDouble() {
        this(true);
    }

    public double getValue(String key) {
        if (validateKey) {
            if(validateKey)
                throw new Error("Waiiiitasec... how come validateKey is true?");
            if (!CollectionsUtil.validKey(key)) {
                throw new IllegalArgumentException("Trie can't accept key '" + key + "'");
            }
        }
        return getTrie(key).value();
    }

    TrieDouble getTrie(String key) {
        final byte[] keyChars = key.intern().getBytes();
        final int keyLength = keyChars.length;
        TrieDouble current = this;
        for (int i = 0; i < keyLength; ++i) {
            char k = (char) (keyChars[i]);
            if (current.branches[k] == null) {
                current.branches[k] = new TrieDouble(validateKey);
            }
            current = current.branches[k];
        }
        return current;
    }

    public void setValue(String key, double d) {
        if (validateKey) {
            if (!CollectionsUtil.validKey(key)) {
                throw new IllegalArgumentException("Trie can't accept key '" + key + "'");
            }
        }
        TrieDouble t = getTrie(key);
        t.value = d;
    }
}
