package com.enigmastation.recommendations;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jottinger
 * Date: Jan 9, 2008
 * Time: 8:21:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Recommendation {
    double getDistance(NestedDictionary dict, Object k, Object k1);

    List<Tuple> getTopMatches(NestedDictionary dict, Object match);

    List<Tuple> getTopMatches(NestedDictionary dict, Object match, int matches);

    List<Tuple> getRecommendations(NestedDictionary dict, Object person);
}
