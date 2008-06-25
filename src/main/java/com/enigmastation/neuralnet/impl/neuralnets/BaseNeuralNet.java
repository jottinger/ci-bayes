package com.enigmastation.neuralnet.impl.neuralnets;

import com.enigmastation.collections.CollectionsUtil;
import com.enigmastation.collections.NestedDictionary;
import com.enigmastation.neuralnet.KeyNotFoundException;
import com.enigmastation.neuralnet.NeuralNet;
import com.enigmastation.neuralnet.NeuralNetDAO;
import com.enigmastation.neuralnet.Resolver;
import com.enigmastation.neuralnet.model.Linkage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.logging.Logger;

public class BaseNeuralNet implements NeuralNet {
    @Autowired
    NeuralNetDAO dao;
    static final int ORIGIN = 0;
    static final int DESTINATION = 1;

    List<Double> ai = new ArrayList<Double>();
    List<Double> ah = new ArrayList<Double>();
    List<Double> ao = new ArrayList<Double>();
    List<Integer> wordIds = new ArrayList<Integer>();
    List<Integer> hiddenIds = new ArrayList<Integer>();
    List<Integer> urlIds = new ArrayList<Integer>();
    NestedDictionary wi = new NestedDictionary();
    NestedDictionary wo = new NestedDictionary();

    public BaseNeuralNet(NeuralNetDAO dao) {
        this.dao = dao;
    }

    public Resolver getDao() {
        return dao;
    }

    public void setDao(NeuralNetDAO dao) {
        this.dao = dao;
    }

    public BaseNeuralNet() {
    }

    public void generateHiddenNode(String[] origins, String[] destinations) {
        String createKey = CollectionsUtil.join("_", (Object[]) origins);
        try {
            dao.getId(createKey);
        } catch (KeyNotFoundException knfe) {
            Integer hiddenid = dao.addKey(createKey);
            for (String origin : origins) {
                Integer oId = dao.addKey(origin);
                setStrength(ORIGIN, oId, hiddenid, 1.0 / origins.length);
            }
            for (String destination : destinations) {
                Integer dId = dao.addKey(destination);
                setStrength(DESTINATION, hiddenid, dId, 0.1);
            }
        }
    }

    private double getStrength(int layer, Integer origin, Integer dest) {
        double value = ((layer == 0) ? -0.2 : 0.0);
        Linkage linkage = dao.getLinkage(layer, origin, dest);
        if (linkage == null) {
            return value;
        }
        return linkage.getStrength();
    }

    private void setStrength(int layer, Integer origin, Integer dest, double v) {
        //log.severe(layer+","+origin+","+dest+","+v);
        dao.setStrength(layer, origin, dest, v);
    }

    List<Integer> getAllHiddenIds(List<Integer> originList, List<Integer> destinationList) {
        Set<Integer> ids = new TreeSet<Integer>();
        //List<T> originList = Arrays.asList(origins);
        //List<T> destinationList = Arrays.asList(destinations);

        for (Integer o : originList) {
            ids.addAll(dao.getHiddenIds(0, o));
            //log.severe(dao.getHiddenIds(0,o).toString());
        }
        for (Integer o : destinationList) {
            ids.addAll(dao.getHiddenIds(1, o));
            //log.severe(dao.getHiddenIds(0,o).toString());
        }
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(ids);
        return list;
    }

    public List<Double> getResult(String[] origins, String[] destinations) {
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
            outputDeltas[k] = dtanh(ao.get(k)) * error;
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
                wo.save(hiddenIds.get(j), urlIds.get(k), wo.get(hiddenIds.get(j)).get(urlIds.get(k)) + N * change);
            }
        }
        for (int i = 0; i < wordIds.size(); i++) {
            for (int j = 0; j < hiddenIds.size(); j++) {
                double change = hiddenDeltas[j] * ai.get(i);
                wi.save(wordIds.get(i), hiddenIds.get(j), wi.get(wordIds.get(i)).get(hiddenIds.get(j)) + N * change);
            }
        }
    }

    /**
     * TODO: This method doesn't work.
     *
     * @param wordIds     wordids
     * @param urlIds      urlids
     * @param selectedUrl selectedurl
     */
    public void trainquery(String[] wordIds, String[] urlIds, String selectedUrl) {
        generateHiddenNode(wordIds, urlIds);
        setupNetwork(wordIds, urlIds);
        feedforward();
        double targets[] = new double[urlIds.length];
        for (int i = 0; i < urlIds.length; i++) {
            if (urlIds[i].equals(selectedUrl)) {
                System.out.println("assigned 1.0 to " + i);
                targets[i] = 1.0;
            } else {
                targets[i] = 0.0;
            }
        }
        backPropagate(targets);
        updateDatabase();
    }

    private void updateDatabase() {
        log.severe("Updating database");
        for (int i = 0; i < wordIds.size(); i++) {
            for (int j = 0; j < hiddenIds.size(); j++) {
                setStrength(0, wordIds.get(i), hiddenIds.get(j), wi.get(wordIds.get(i)).get(hiddenIds.get(j)));
            }
        }
        for (int j = 0; j < hiddenIds.size(); j++) {
            for (int k = 0; k < urlIds.size(); k++) {
                setStrength(1, hiddenIds.get(j), urlIds.get(k), wo.get(hiddenIds.get(j)).get(urlIds.get(k)));
            }
        }
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

    private void setupNetwork(String[] origins, String[] destinations) {
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
                double d = getStrength(0, i, j);
                //log.severe("strength of "+0+","+i+","+j+":"+d);
                wi.save(i, j, d);
            }
        }
        for (Integer i : hiddenIds) {
            for (Integer j : urlIds) {
                double d = getStrength(1, i, j);
                //log.severe("strength of "+1+","+i+","+j+":"+d);
                wo.save(i, j, d);
            }
        }
    }

    private List<Integer> populatedList(String[] origins) {
        List<Integer> list = new ArrayList<Integer>();
        for (String origin : origins) {
            list.add(dao.getId(origin));
        }
        return list;
    }

    Logger log = Logger.getLogger(this.getClass().getName());

    public void dump(int layer) {
        Linkage[] linkages = dao.getLinkages(layer);
        for (Linkage l : linkages) {
            log.severe(l.toString());
        }
    }
}
