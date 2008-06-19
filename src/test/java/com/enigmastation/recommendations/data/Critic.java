package com.enigmastation.recommendations.data;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jun 19, 2008
 * Time: 12:18:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Critic {
    Map<String, Double> scores=new HashMap<String, Double>();
    String name;
    public Critic(String name, Object ... args) {
        this.name=name;
        for(int i=0; i<args.length; i+=2) {
            if(!(i+1>args.length)) {
                String key= (String) args[i];
                Double score= (Double) args[i+1];
                scores.put(key, score);
            }
        }
    }

    public double getScore(String key) {
        if(scores.containsKey(key)) {
            return scores.get(key);
        }
        return 0;
    }
    public void setScore(String key, double d) {
        scores.put(key, d);
    }
    
    public Map<String, Double> getScores() {
        return Collections.unmodifiableMap(scores);
    }
}
