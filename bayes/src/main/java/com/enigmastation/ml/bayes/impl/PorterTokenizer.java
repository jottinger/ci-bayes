package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.Tokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PorterTokenizer implements Tokenizer {
    @Override
    public List<Object> tokenize(Object source) {
        List<Object> tokens = new ArrayList<>(source.toString().length() / 5);
        org.apache.lucene.analysis.Tokenizer tokenizer =
                new StandardTokenizer(Version.LUCENE_34,
                        new StringReader(source.toString()));
        CharTermAttribute charTermAttribute = tokenizer.getAttribute(CharTermAttribute.class);
        PorterStemFilter filter = new PorterStemFilter(tokenizer);
        try {
            while (filter.incrementToken()) {
                String term = charTermAttribute.toString().toLowerCase();
                if (term.length() > 2) {
                    tokens.add(term);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Should not happen: " + e.getMessage(), e);
        }
        return tokens;
    }
}
