package com.enigmastation.extractors.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import com.google.common.collect.Sets;

/**
 * This is an implementation of a wordlister that uses Lucene's Snowball
 * stemming implementation. It's... okay. In the training corpus, it was just
 * as accurate as the SimpleWordLister, but almost twice as slow:
 * <p/>
 * 2146 items, 36 misses: 98.32% accuracy and 6ms/item.
 * <p/>
 * Yuck. Use this puppy ONLY if you really need Snowball.
 */
public class LuceneStemmingWordLister extends SimpleWordLister {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2079281327726910236L;
	static final Set<String> emptySet = Sets.newHashSet();

    public void addWords(Object obj, Collection<String> features) {
        String document = obj.toString().toLowerCase();
        StandardTokenizer tokenizer = new StandardTokenizer(Version.LUCENE_30,new StringReader(document));
        tokenizer.setMaxTokenLength(20);
        SnowballFilter psf = new SnowballFilter(tokenizer, "English");
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
    }
}