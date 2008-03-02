package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.WordLister;
import javolution.util.FastSet;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation of a stemming word lister comes from Kevin Burton.
 *
 * It relies on a Porter stemming algorithm for stems.
 *
 * This is probably the best match of speed/accuracy so far;
 * over the training corpus, it yielded 37 misses over 2146 items
 * in 6797 ms, for an accuracy of 98.27% and 3.2ms per item.
 */
public class StemmingWordLister implements WordLister {

    /**
     * Minimum length for terms.
     */
    public static int MIN_LENGTH = 2;

    /**
     * Maximum length for terms.
     */
    public static int MAX_LENGTH = 20;

    public static int STEMMER_MIN_LENGTH = 3;

    public Set<String> getUniqueWords(Object obj) {
        String document = obj.toString();

        //NOTE: using a thread local Pattern cache would be more efficient.
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(document);
        Set<String> features = new FastSet<String>();
        PorterStemmer stemmer = new PorterStemmer();

        while (m.find()) {

            String w = m.group();

            if (w.length() <= MIN_LENGTH)
                continue;

            if (w.length() >= MAX_LENGTH)
                continue;

            //NOTE: this should be customizable.
            w = w.toLowerCase();

            if (w.length() > STEMMER_MIN_LENGTH)
                w = stemmer.stem(w);

            features.add(w);
        }
        return features;
    }
}