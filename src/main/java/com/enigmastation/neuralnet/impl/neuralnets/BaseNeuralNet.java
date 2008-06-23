package com.enigmastation.neuralnet.impl.neuralnets;

import com.enigmastation.neuralnet.NeuralNet;
import com.enigmastation.neuralnet.Resolver;
import com.enigmastation.neuralnet.KeyNotFoundError;
import com.enigmastation.neuralnet.impl.resolvers.BaseResolver;
import com.enigmastation.collections.CollectionsUtil;
import com.enigmastation.collections.NestedDictionary;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseNeuralNet<T> implements NeuralNet<T> {
    Resolver<T> resolver;
    static final int ORIGIN = 0;
    static final int DESTINATION = 1;
    NestedDictionary wordhidden = new NestedDictionary();
    NestedDictionary hiddenurl = new NestedDictionary();
    Resolver<String> hiddenidresolver = new BaseResolver<String>();

    public BaseNeuralNet(Resolver<T> resolver) {
        this.resolver = resolver;
    }

    public void generateHiddenNode(T[] origins, T[] destinations) {
        String createKey = CollectionsUtil.join("_", origins);
        try {
            hiddenidresolver.getId(createKey);
        } catch (KeyNotFoundError knfe) {
            Integer hiddenid = hiddenidresolver.addKey(createKey);
            for (T origin : origins) {
                Integer oId = resolver.addKey(origin);
                setStrength(ORIGIN, oId, hiddenid, 1.0 / origins.length);
            }
            for (T destination : destinations) {
                Integer dId = resolver.addKey(destination);
                setStrength(DESTINATION, hiddenid, dId, 0.1);
            }
        }
    }

    private double getStrength(int layer, Integer origin, Integer dest) {
        double value = ((layer == 0) ? -0.2 : 0.0);
        NestedDictionary dict = (layer == 0 ? wordhidden : hiddenurl);
        Map<Object, Double> nMap = dict.get(origin);
        if (nMap != null) {
            Double d = nMap.get(dest);
            if (d != null) {
                value = d.doubleValue();
            }
        }
        return value;
    }

    private void setStrength(int layer, Integer origin, Integer dest, double v) {
        NestedDictionary dict = (layer == 0 ? wordhidden : hiddenurl);
        dict.save(origin, dest, v);
    }

    List<Integer> getAllHiddenIds(List<Integer> originList, List<Integer> destinationList) {
        List<Integer> ids = new ArrayList<Integer>();
        //List<T> originList = Arrays.asList(origins);
        //List<T> destinationList = Arrays.asList(destinations);
        for (Integer o : originList) {
            Map<Object, Double> mop = wordhidden.get(o);
            if (mop != null) {
                for (Object p : mop.keySet()) {
                    if(!ids.contains((Integer)p)) {
                        ids.add((Integer) p);
                    }
                }
            }
        }
        for (Object o : hiddenurl.keySet()) {
            Map<Object, Double> mop = hiddenurl.get(o);
            if (mop != null) {
                for (Object p : mop.keySet()) {
                    if(destinationList.contains(p)) {
                        ids.add((Integer) p);
                    }
                }
            }
        }
        return ids;
    }

    public List<Double> getResult(T[] origins, T[] destinations) {
        List<Double> ai = new ArrayList<Double>();
        List<Double> ah = new ArrayList<Double>();
        List<Double> ao = new ArrayList<Double>();
        List<Integer> wordIds = new ArrayList<Integer>();
        List<Integer> hiddenIds = new ArrayList<Integer>();
        List<Integer> urlIds = new ArrayList<Integer>();
        NestedDictionary wi = new NestedDictionary();
        NestedDictionary wo = new NestedDictionary();

        setupNetwork(origins, destinations, ai, ah, ao, wi, wo, wordIds, hiddenIds, urlIds);
        return feedforward(ai, hiddenIds, wordIds, urlIds, wi, wo, ah, ao);
    }

    List<Double> feedforward(List<Double> ai,
                             List<Integer> hiddenIds,
                             List<Integer> wordIds,
                             List<Integer> urlIds,
                             NestedDictionary wi,
                             NestedDictionary wo,
                             List<Double> ah,
                             List<Double> ao) {
        List<Double> results = new ArrayList<Double>();
        for (int i = 0; i < ai.size(); i++) {
            ai.set(i, 1.0);
        }
        for (int j=0;j<hiddenIds.size();j++) {
            double sum = 0.0;
            for (int i=0;i<wordIds.size();i++) {
                Map<Object, Double> mop=wi.get(wordIds.get(i));
                double d=mop.get(hiddenIds.get(j));
                double aid=ai.get(i);
                sum += aid * d;
            }
            ah.set(j, Math.tanh(sum));
        }
        for (int k = 0; k < urlIds.size(); k++) {
            double sum = 0.0;
            for (int j = 0; j < hiddenIds.size(); j++) {
                Map<Object, Double> mop=wo.get(hiddenIds.get(j));
                double d=ah.get(j);
                sum += d * mop.get(urlIds.get(k));
            }
            ao.set(k, Math.tanh(sum));
            results.add(Math.tanh(sum));
        }
        return results;
    }

    private void setupNetwork(T[] origins, T[] destinations, List<Double> ai, List<Double> ah, List<Double> ao,
                              NestedDictionary wi, NestedDictionary wo, List<Integer> wordIds, List<Integer> hiddenIds, List<Integer> urlIds) {
        wordIds.addAll(populatedList(origins));
        urlIds.addAll(populatedList(destinations));
        hiddenIds.addAll(getAllHiddenIds(wordIds, urlIds));

        for (Integer i : wordIds) {
            ai.add(1.0);
        }
        for (Integer i : hiddenIds) {
            ah.add(1.0);
        }
        for (Integer i : urlIds) {
            ao.add(1.0);
        }
        for (Integer i : wordIds) {
            for (Integer j : hiddenIds) {
                wi.save(i, j, getStrength(0, i, j));
            }
        }
        for (Integer i : hiddenIds) {
            for (Integer j : urlIds) {
                wo.save(i, j, getStrength(1, i, j));
            }
        }
    }

    private List<Integer> populatedList(T[] origins) {
        List<Integer> list = new ArrayList<Integer>();
        for (T origin : origins) {
            list.add(resolver.getId(origin));
        }
        return list;
    }

    public void dump(int layer) {
        NestedDictionary dict = (layer == 0 ? wordhidden : hiddenurl);
        for (Object o : dict.keySet()) {
            Map<Object, Double> mop = dict.get(o);
            for (Object p : mop.keySet()) {
                System.out.printf("%s, %s, %f%n", o.toString(), p.toString(), mop.get(p));
            }
        }
    }
}
