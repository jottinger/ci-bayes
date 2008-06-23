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
    List<Double> ai = new ArrayList<Double>();
    List<Double> ah = new ArrayList<Double>();
    List<Double> ao = new ArrayList<Double>();
    List<Integer> wordIds = new ArrayList<Integer>();
    List<Integer> hiddenIds = new ArrayList<Integer>();
    List<Integer> urlIds = new ArrayList<Integer>();
    NestedDictionary wi = new NestedDictionary();
    NestedDictionary wo = new NestedDictionary();

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
                    if (!ids.contains((Integer) p)) {
                        ids.add((Integer) p);
                    }
                }
            }
        }
        for (Object o : hiddenurl.keySet()) {
            Map<Object, Double> mop = hiddenurl.get(o);
            if (mop != null) {
                for (Object p : mop.keySet()) {
                    if (destinationList.contains(p)) {
                        ids.add((Integer) p);
                    }
                }
            }
        }
        return ids;
    }

    public List<Double> getResult(T[] origins, T[] destinations) {
        ai = new ArrayList<Double>();
        ah = new ArrayList<Double>();
        ao = new ArrayList<Double>();
        wordIds = new ArrayList<Integer>();
        hiddenIds = new ArrayList<Integer>();
        urlIds = new ArrayList<Integer>();
        wi = new NestedDictionary();
        wo = new NestedDictionary();

        setupNetwork(origins, destinations);
        return feedforward();
    }


    double dtanh(double y) {
        return 1.0 - y * y;
    }

    void backPropagate(double[] targets, double N) {
        double[] outputDeltas = new double[urlIds.size()];
        double[] hiddenDeltas = new double[hiddenIds.size()];

        for (int k = 0; k < urlIds.size(); k++) {
            double error = targets[k] - ao.get(k);
            outputDeltas[k] = dtanh(ao.get(k) * error);
        }
        for (int j = 0; j < hiddenIds.size(); j++) {
            double error = 0.0;
            for (int k = 0; k < urlIds.size(); k++) {
                error += (outputDeltas[k] * wo.get(hiddenIds.get(j)).get(urlIds.get(k)));
            }
            hiddenDeltas[j] = dtanh(ah.get(j)) * error;
        }

        for (int j = 0; j < hiddenIds.size(); j++) {
            for (int k = 0; k < urlIds.size(); k++) {
                double change = outputDeltas[k] * ah.get(j);
                wo.save(j, k, wo.get(hiddenIds.get(j)).get(urlIds.get(k)) + N * change);
            }
        }
        for (int i = 0; i < wordIds.size(); i++) {
            for (int j = 0; j < hiddenIds.size(); j++) {
                double change = hiddenDeltas[j] * ai.get(i);
                wi.save(i, j, wi.get(wordIds.get(i)).get(hiddenIds.get(j)) + N * change);
            }
        }
    }

    /**
     * TODO: This method doesn't work.
     */
    public void trainquery(T[] wordIds, T[] urlIds, T selectedUrl) {
        generateHiddenNode(wordIds, urlIds);
        setupNetwork(wordIds, urlIds);
        feedforward();
        double targets[]=new double[urlIds.length];
        for(int i=0;i<urlIds.length;i++) {
            if(urlIds[i].equals(selectedUrl)) {
                System.out.println("assigned 1.0 to "+i);
                targets[i]=1.0;
            } else {
                targets[i]=0.0;
            }
        }
        backPropagate(targets);
    }

    void backPropagate(double[] targets) {
        backPropagate(targets, 0.5);
    }

    List<Double> feedforward() {
        List<Double> results = new ArrayList<Double>();
        for (int i = 0; i < ai.size(); i++) {
            ai.set(i, 1.0);
        }
        for (int j = 0; j < hiddenIds.size(); j++) {
            double sum = 0.0;
            for (int i = 0; i < wordIds.size(); i++) {
                Map<Object, Double> mop = wi.get(wordIds.get(i));
                double d = mop.get(hiddenIds.get(j));
                double aid = ai.get(i);
                sum += aid * d;
            }
            ah.set(j, Math.tanh(sum));
        }
        for (int k = 0; k < urlIds.size(); k++) {
            double sum = 0.0;
            for (int j = 0; j < hiddenIds.size(); j++) {
                Map<Object, Double> mop = wo.get(hiddenIds.get(j));
                double d = ah.get(j);
                sum += d * mop.get(urlIds.get(k));
            }
            ao.set(k, Math.tanh(sum));
            results.add(Math.tanh(sum));
        }
        return results;
    }

    private void setupNetwork(T[] origins, T[] destinations) {
        wordIds.clear();
        wordIds.addAll(populatedList(origins));
        urlIds.clear();
        urlIds.addAll(populatedList(destinations));
        hiddenIds.clear();
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
