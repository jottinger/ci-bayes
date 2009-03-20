package com.enigmastation.extractors.impl;

import javolution.util.FastSet;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

import com.enigmastation.extractors.WordLister;

/**
 * This is an implementation of a wordlister that uses Lucene's Snowball
 * stemming implementation. It's... okay. In the training corpus, it was just
 * as accurate as the SimpleWordLister, but almost twice as slow:
 * <p/>
 * 2146 items, 36 misses: 98.32% accuracy and 6ms/item.
 * <p/>
 * Yuck. Use this puppy ONLY if you really need Snowball.
 */
public class LuceneStemmingWordLister implements WordLister {
    static final Set<String> emptySet = new FastSet<String>();

    public Set<String> getUniqueWords(Object obj) {
        Set<String> features = new FastSet<String>();
        String document = obj.toString().toLowerCase();
        StandardTokenizer tokenizer = new StandardTokenizer(new StringReader(document));
        tokenizer.setMaxTokenLength(20);
        TokenFilter psf = new SnowballFilter(tokenizer, "English");
        Token t;
        StringBuilder sb = new StringBuilder();
        try {
            while ((t = psf.next()) != null) {
                sb.setLength(0);
                sb.append(t.termBuffer(), 0, t.termLength());
                //System.out.println(sb.toString());
                features.add(sb.toString());
            }
        } catch (IOException e) {
            // should never happen! We're reading a flippin' STRING!
            e.printStackTrace();
        }
        return features;
    }
}