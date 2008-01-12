package com.enigmastation.recommendations.item;

import com.enigmastation.recommendations.NestedDictionary;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jottinger
 * Date: Jan 9, 2008
 * Time: 8:00:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceRecommendationImpl extends RecommendationImpl {
    public double getDistance(NestedDictionary dict, Object key1, Object key2) {
        List<Object> si = getMatchingEntriesList(dict, key1, key2);
        if(si.size()==0) {
            return 0;
        }
        double sum_of_squares=0;

        for(Object subKey:si) {
            sum_of_squares+=Math.pow(dict.get(key1).get(subKey)-dict.get(key2).get(subKey),2.0);
        }

        return 1.0/(1.0+sum_of_squares);
    }

}
