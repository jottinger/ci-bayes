package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.WordLister;
import javolution.util.FastSet;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a very simple class to return a list of tokens from a string. It's very simple. It returns all words,
 * lowercased, from a string, assuming they're between three and 19 characters long. It doesn't remove particularly
 * common words like "the," nor does it manage conjugations or tenses. 
 *
 * It was ported from Python contained in the book
 * "<a href="http://www.oreilly.com/catalog/9780596529321/index.html">Programming Collective Intelligence</a>,"
 * by Toby Segaran.
 *
 * The "porting" part probably wasn't necessary, but I wanted to preserve Mr. Segaran's organization.
 *
 * Note: the default implementation uses Javolution, for speed. There are special cases in the classifiers that
 * will change iteration behavior if the Fast* collections are used.
 *
 * Modified to include better pattern matching (thanks, Kevin!).
 *
 * This was superfastical, yielding 36 misses over 2146 items.
 *
 * That's a 98.32% hit, and at 5657ms, it's 2.68 ms per item. 
 * 
 * @version $Revision: 36 $
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>

 */
public class SimpleWordLister implements WordLister {
    public static final int MIN_LENGTH=2;
    public static final int MAX_LENGTH=20;
    public static final Pattern p= Pattern.compile("\\w++");

    public Set<String> getUniqueWords(Object obj) {
        String document=obj.toString().toLowerCase();
        //Pattern p= Pattern.compile("\\w++");
        Matcher m=p.matcher(document);

        Set<String> features=new FastSet<String>();
        while(m.find()) {
            String w=m.group();
            if(w.length()>MIN_LENGTH && w.length()<MAX_LENGTH) {
            features.add(w);
            }
        }
        return features;
    }
}
