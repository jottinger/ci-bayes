package com.enigmastation.recommendations;

import com.enigmastation.collections.NestedDictionaryStringStringDouble;
import com.enigmastation.collections.Tuple;

import java.util.List;

/**
 * Recommendation
 */
public interface Recommendation {
    double getDistance(NestedDictionaryStringStringDouble dict, Object k, Object k1);

    List<Tuple> getTopMatches(NestedDictionaryStringStringDouble dict, Object match);

    @SuppressWarnings({"SameParameterValue"})
    List<Tuple> getTopMatches(NestedDictionaryStringStringDouble dict, Object match, int matches);

    List<Tuple> getRecommendations(NestedDictionaryStringStringDouble dict, Object person);
}
