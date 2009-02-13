package com.enigmastation.recommendations.item;

import com.enigmastation.collections.NestedDictionaryStringStringDouble;

import java.util.List;

public class PearsonCorrelationCoefficientImpl extends RecommendationImpl {
    public double getDistance(NestedDictionaryStringStringDouble dict, Object p1, Object p2) {
        List<Object> si = getMatchingEntriesList(dict, p1, p2);
        int n = si.size();
        if (n == 0) {
            return 0;
        }

        double sum1 = 0;
        double sum1sq = 0;
        for (Object it : si) {
            sum1 += dict.get(p1).get(it);
            sum1sq += Math.pow(dict.get(p1).get(it), 2.0);
        }

        double sum2 = 0;
        double sum2sq = 0;
        for (Object it : si) {
            sum2 += dict.get(p2).get(it);
            sum2sq += Math.pow(dict.get(p2).get(it), 2.0);
        }

        double pSum = 0;
        for (Object it : si) {
            pSum += (dict.get(p1).get(it) * dict.get(p2).get(it));
        }

        double num = pSum - (sum1 * sum2 / n);
        double den = Math.sqrt(((sum1sq - (sum1 * sum1) / n)) * (sum2sq - ((sum2 * sum2) / n)));
        if (den == 0) {
            return 0;
        }
        return num / den;
    }
}
