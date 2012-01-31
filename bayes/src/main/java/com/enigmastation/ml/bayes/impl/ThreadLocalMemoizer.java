package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.Tokenizer;

import java.util.List;

public class ThreadLocalMemoizer {
    List<Object> features;
    Object corpus;

    public Object getCorpus() {
        return corpus;
    }

    public void setCorpus(Object corpus) {
        this.corpus = corpus;
    }

    public List<Object> getFeatures(Object corpus) {
        return features;
    }

    public void setFeatures(Object corpus, Tokenizer tokenizer) {
        if (!this.corpus.equals(corpus)) {
            this.corpus = corpus;
            this.features = tokenizer.tokenize(corpus);
        }
    }
}
