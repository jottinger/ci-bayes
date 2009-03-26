package com.enigmastation.extractors.impl;

import com.enigmastation.extractors.WordLister;
import javolution.util.FastSet;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a very simple class to return a list of tokens from a string. It's very simple. It returns all words,
 * lowercased, from a string, assuming they're between three and 19 characters long. It doesn't remove particularly
 * common words like "the," nor does it manage conjugations or tenses.
 * <p/>
 * It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 * <p/>
 * The "porting" part probably wasn't necessary, but I wanted to preserve Mr. Segaran's organization.
 * <p/>
 * Note: the default implementation uses Javolution, for speed. There are special cases in the classifiers that
 * will change iteration behavior if the Fast* collections are used.
 * <p/>
 * Modified to include better pattern matching (thanks, Kevin!).
 * <p/>
 * This was superfastical, yielding 36 misses over 2146 items.
 * <p/>
 * That's a 98.32% hit, and at 5657ms, it's 2.68 ms per item.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 36 $
 */
public class SimpleWordLister implements WordLister {
    public static final int MIN_LENGTH = 2;
    public static final int MAX_LENGTH = 20;
    public static final Pattern p = Pattern.compile("\\w++");

    /**
     * This implementation uses very simple pattern matching to get words from a string.
     *
     * @param obj        The base object, converted to a string via toString()
     * @param collection a collection type (List or Set) to hold the generated words
     */
    public void addWords(Object obj, Collection<String> collection) {
        String document = obj.toString().toLowerCase();
        //Pattern p= Pattern.compile("\\w++");
        Matcher m = p.matcher(document);
        while (m.find()) {
            String w = m.group();
            if (w.length() > MIN_LENGTH && w.length() < MAX_LENGTH) {
                collection.add(w);
            }
        }
    }

    /**
     * This is a simple method that wraps addWords(), using a Set as the base collection type. This will enforce
     * uniqueness of the words as they come back from the document.
     *
     * @param obj the base object, converted to a String via toString()
     * @return a Set of unique words
     */
    public Set<String> getUniqueWords(Object obj) {
        Set<String> features = new FastSet<String>();
        addWords(obj, features);
        return features;
    }
}