package com.enigmastation.recommendations.item;

import com.enigmastation.recommendations.NestedDictionary;
import com.enigmastation.recommendations.Recommendation;
import com.enigmastation.recommendations.Tuple;
import javolution.util.FastList;
import javolution.util.FastMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * RecommendationImpl
 */
public abstract class RecommendationImpl implements Recommendation {
    protected List<Object> getMatchingEntriesList(NestedDictionary dict, Object key1, Object key2) {
        List<Object> si = new FastList<Object>();
        for (Object subKey : dict.get(key1).keySet()) {
            if (dict.get(key2).containsKey(subKey)) {
                si.add(subKey);
            }
        }
        return si;
    }

    public List<Tuple> getTopMatches(NestedDictionary dict, Object match) {
        return getTopMatches(dict, match, 3);
    }

    public List<Tuple> getTopMatches(NestedDictionary dict, Object person, int matches) {
        List<Tuple> scores = new FastList<Tuple>();
        for (Object other : dict.keySet()) {
            if (!other.equals(person)) {
                Tuple t=new Tuple(other, this.getDistance(dict, other, person));
                //System.out.println(t);
                scores.add(t);
            }
        }
        Collections.sort(scores, new Comparator<Tuple>() {
            public int compare(Tuple o1, Tuple o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                }
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                }
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return scores.subList(0, matches);
    }

    public List<Tuple> getRecommendations(NestedDictionary dict, Object person) {
        Map<Object, Double> totals=new FastMap<Object, Double>();
        Map<Object, Double> simSums=new FastMap<Object, Double>();
        for(Object other:dict.keySet()) {
            if(!other.equals(person)) {
                double sim=this.getDistance(dict, person, other);

                if(sim>=0) {
                    for(Object item:dict.get(other).keySet()) {
                        if(!dict.get(person).containsKey(item) ||
                                dict.get(person).get(item)==0.0) {
                            if(!totals.containsKey(item)) {
                                totals.put(item, 0.0);
                            }
                            totals.put(item, totals.get(item)+dict.get(other).get(item)*sim);
                            if(!simSums.containsKey(item)) {
                                simSums.put(item, 0.0);
                            }
                            simSums.put(item, simSums.get(item)+sim);
                        }
                    }
                }
            }
        }
        List<Tuple> rankings=new FastList<Tuple>();
        for(Object item:totals.keySet()) {
            double d=totals.get(item)/simSums.get(item);
            rankings.add(new Tuple(item, d));
        }
        Collections.sort(rankings,new Comparator<Tuple>() {
            public int compare(Tuple o1, Tuple o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                }
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                }
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return rankings;
    }
}
