package com.enigmastation.recommendations;

import java.util.List;

/**
 * Recommendation
 */
public interface Recommendation {
    double getDistance(NestedDictionary dict, Object k, Object k1);

    List<Tuple> getTopMatches(NestedDictionary dict, Object match);

    @SuppressWarnings({"SameParameterValue"})
    List<Tuple> getTopMatches(NestedDictionary dict, Object match, int matches);

    List<Tuple> getRecommendations(NestedDictionary dict, Object person);
}
