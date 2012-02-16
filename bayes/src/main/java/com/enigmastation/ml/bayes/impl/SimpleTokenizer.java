package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleTokenizer implements Tokenizer {
    @Override
    public List<Object> tokenize(Object source) {
        String src = source.toString();
        List<Object> tokens = new ArrayList<>(src.length() / 6);
        Scanner scanner = new Scanner(src);
        while (scanner.hasNext("\\S*")) {
            tokens.add(scanner.next("\\S*"));
        }
        return tokens;
    }
}
