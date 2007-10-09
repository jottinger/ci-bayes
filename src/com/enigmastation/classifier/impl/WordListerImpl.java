package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.WordLister;
import javolution.util.FastSet;

import java.util.Set;
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
 * @version $Revision$
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>

 */
public class WordListerImpl implements WordLister {
    public Set<String> getUniqueWords(String document) {
        Pattern p= Pattern.compile("\\W");
        String[] words=p.split(document.toLowerCase());
        Set<String> features=new FastSet<String>();
        for(String w:words) {
            if(w.length()>2 && w.length()<20) {
            features.add(w);
            }
        }
        return features;  
    }
}
