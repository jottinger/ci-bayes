package com.enigmastation.extractors.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

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
        OffsetAttribute offsetAttribute = tokenizer.getAttribute(OffsetAttribute.class);
        TermAttribute termAttribute = tokenizer.getAttribute(TermAttribute.class);
        try {
            while(tokenizer.incrementToken()) {
               features.add(termAttribute.term());
            }
        } catch(IOException e) {
            throw new Error("IOException where no IOException should be", e);
        }
    }
}