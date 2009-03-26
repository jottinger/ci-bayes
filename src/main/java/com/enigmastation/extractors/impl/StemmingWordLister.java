package com.enigmastation.extractors.impl;

import javolution.util.FastSet;

import java.util.Set;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.enigmastation.extractors.WordLister;

/**
 * This implementation of a stemming word lister comes from Kevin Burton.
 * <p/>
 * It relies on a Porter stemming algorithm for stems.
 * <p/>
 * This is probably the best match of speed/accuracy so far;
 * over the training corpus, it yielded 37 misses over 2146 items
 * in 6797 ms, for an accuracy of 98.27% and 3.2ms per item.
 */
public class StemmingWordLister extends SimpleWordLister {

    /**
     * Minimum length for terms.
     */
    public static int MIN_LENGTH = 2;

    /**
     * Maximum length for terms.
     */
    public static int MAX_LENGTH = 20;

    public static int STEMMER_MIN_LENGTH = 3;
    static Pattern p = Pattern.compile("\\w+");

    public void addWords(Object obj, Collection<String> features) {
        String document = obj.toString();

        Matcher m = p.matcher(document);
        PorterStemmer stemmer = new PorterStemmer();

        while (m.find()) {
            String w = m.group();

            if (w.length() <= MIN_LENGTH) {
                continue;
            }

            if (w.length() >= MAX_LENGTH) {
                continue;
            }

            //NOTE: this should be customizable.
            w = w.toLowerCase();

            if (w.length() > STEMMER_MIN_LENGTH) {
                w = stemmer.stem(w);
            }

            features.add(w);
        }
    }
}