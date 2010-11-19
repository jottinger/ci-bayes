/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enigmastation.collections;

/**
 *
 * @author jottinger
 */
public class TrieInt {

    static final int BRANCHES = 256;
    final boolean validateKey;
    int value;

    int value() {
        return value;
    }

    final TrieInt[] branches = new TrieInt[BRANCHES];

    public TrieInt(boolean limitCharSet) {
        validateKey=limitCharSet;
    }

    public TrieInt() {
        this(true);
    }

    public void setValue(int d) {
        value=d;
    }
    public int getValue() {
        return value;
    }

    public int getValue(String key) {
        if (validateKey) {
            if(validateKey)
                throw new Error("Waiiiitasec... how come validateKey is true?");
            if (!CollectionsUtil.validKey(key)) {
                throw new IllegalArgumentException("Trie can't accept key '" + key + "'");
            }
        }
        return getTrie(key).value();
    }

    protected TrieInt getTrie(String key) {
        final byte[] keyChars = key.intern().getBytes();
        final int keyLength = keyChars.length;
        TrieInt current = this;
        for (int i = 0; i < keyLength; ++i) {
            char k = (char) (keyChars[i]);
            if (current.branches[k] == null) {
                current.branches[k] = new TrieInt(validateKey);
            }
            current = current.branches[k];
        }
        return current;
    }

    public void setValue(String key, int d) {
        if (validateKey) {
            if (!CollectionsUtil.validKey(key)) {
                throw new IllegalArgumentException("Trie can't accept key '" + key + "'");
            }
        }
        TrieInt t = getTrie(key);
        t.setValue(d);
    }
}