package com.enigmastation.ml.bayes;

import java.util.List;

public interface Tokenizer {
  List<Object> tokenize(Object source);
}
