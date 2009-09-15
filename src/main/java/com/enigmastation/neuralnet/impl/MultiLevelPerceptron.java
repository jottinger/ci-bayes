package com.enigmastation.neuralnet.impl;

import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.WordListerFactory;
import com.enigmastation.extractors.impl.StemmingWordListerFactory;
import com.enigmastation.neuralnet.NetworkModelFactory;
import com.enigmastation.neuralnet.NeuralNetwork;
import com.enigmastation.resolvers.Resolver;
import com.enigmastation.resolvers.ResolverFactory;
import com.enigmastation.resolvers.impl.MemoryResolverFactory;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: how to persist the maps of synapses? THIS IS PRETTY IMPORTANT.
 */
public class MultiLevelPerceptron implements NeuralNetwork {
    private SynapseMap synapsesForward;
    private SynapseMap synapsesBackward;
    private Resolver resolver;
    private WordLister wordLister;
    private WordListerFactory wordListerFactory;
    private ResolverFactory resolverFactory;
    private boolean initialized = false;
    private NetworkModelFactory networkModelFactory;

    protected synchronized void init() {
        if (!initialized) {
            if (getWordListerFactory() == null) {
                setWordListerFactory(new StemmingWordListerFactory());
            }
            setWordLister(getWordListerFactory().build());
            if (getResolverFactory() == null) {
                setResolverFactory(new MemoryResolverFactory());
            }
            setResolver(getResolverFactory().build());
            if (getNetworkModelFactory() == null) {
                setNetworkModelFactory(new BasicNetworkModelFactory());
            }
            synapsesForward = getNetworkModelFactory().buildForwardMap();
            synapsesBackward = getNetworkModelFactory().buildBackwardMap();
        }
    }

    public WordLister getWordLister() {
        return wordLister;
    }

    public void setWordLister(WordLister wordLister) {
        this.wordLister = wordLister;
    }

    public NetworkModelFactory getNetworkModelFactory() {
        return networkModelFactory;
    }

    public void setNetworkModelFactory(NetworkModelFactory networkModelFactory) {
        this.networkModelFactory = networkModelFactory;
    }

    public WordListerFactory getWordListerFactory() {
        return wordListerFactory;
    }

    public void setWordListerFactory(WordListerFactory wordListerFactory) {
        this.wordListerFactory = wordListerFactory;
    }

    public ResolverFactory getResolverFactory() {
        return resolverFactory;
    }

    public void setResolverFactory(ResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
    }

    public Resolver getResolver() {
        return resolver;
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    /*
    public void display() {
        Graph<String, Double> g = new SparseMultigraph<String, Double>();
        for (Map<Integer, Synapse> map : synapsesForward.values()) {
            for (Synapse synapse : map.values()) {
                if (!g.containsVertex(resolver.getKey(synapse.getSource()))) {
                    g.addVertex(resolver.getKey(synapse.getSource()));
                }
                if (!g.containsVertex(resolver.getKey(synapse.getDestination()))) {
                    g.addVertex(resolver.getKey(synapse.getDestination()));
                }
                double weight = synapse.getWeight();
                while (g.containsEdge(weight)) {
                    weight += 0.000001;
                }
                g.addEdge(weight,
                        resolver.getKey(synapse.getSource()),
                        resolver.getKey(synapse.getDestination()));
            }

        }
        //GraphView sgv = new SimpleGraphView(); //We create our graph in here
        Layout<String, Double> layout = new CircleLayout<String, Double>(g);  //   <String, Double>(g);
        layout.setSize(new Dimension(300, 300)); // sets the initial size of the space
        BasicVisualizationServer<String, Double> vv =
                new BasicVisualizationServer<String, Double>(layout);
        vv.setPreferredSize(new Dimension(750, 750)); //Sets the viewing area size
        vv.scaleToLayout(new edu.uci.ics.jung.visualization.control.LayoutScalingControl());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
    */

    public static void main(String[] args) {
        MultiLevelPerceptron network;

        network = new MultiLevelPerceptron();
        for (int i = 0; i < 30; i++) {
            network.train("good", "Nobody owns the water.");
            network.train("good", "the quick rabbit jumps fences");
            network.train("bad", "buy pharmaceuticals now");
            network.train("bad", "make quick money in the online casino");
            network.train("good", "the quick brown fox jumps");
        }
        System.out.println("quick rabbit:" + network.analyze("good bad", "quick rabbit"));
    }

    public void train(String conclusions, String corpus) {
        init();
        List<String> words = getWords(corpus);
        if (words.size() == 0) {
            // don't bother if the training corpus is empty.
            return;
        }
        Collections.sort(words);

        Set<Integer> wordIds = getUniqueWordIds(getWordIds(words));

        int idForHiddenNode = getResolver().getIdForKey(buildHiddenKey(words));
        List<String> conclusionSet = getWords(conclusions);
        Set<Integer> conclusionIds = getUniqueWordIds(getWordIds(conclusionSet));

        // first let's build the structure for id -> hidden
        // this means back and forth.
        Map<Integer, Synapse> backList = getBackList(idForHiddenNode);

        double strength = 1.0 / words.size();
        for (String word : words) {
            int id = getResolver().getIdForKey(word);
            addGraphNodes(backList, id, idForHiddenNode, strength);
        }


        for (int k : conclusionIds) {
            // now let's do the hidden node -> output mapping, which is MUCH simpler.
            addGraphNodes(idForHiddenNode, k);
        }

        // the graph is complete.
        // Now we apply feed forward and back propagation.
        Set<Integer> hiddenIds = getAllHiddenIds(conclusionIds, wordIds);

        Map<Integer, Double> ai = new FastMap<Integer, Double>();
        Map<Integer, Double> ah = new FastMap<Integer, Double>();
        Map<Integer, Double> ao = new FastMap<Integer, Double>();

        Map<Integer, Map<Integer, Double>> wi = new FastMap<Integer, Map<Integer, Double>>();
        Map<Integer, Map<Integer, Double>> wo = new FastMap<Integer, Map<Integer, Double>>();

        setupNetwork(wordIds, hiddenIds, conclusionIds, ai, ah, ao, wi, wo);
        feedForward(wordIds, hiddenIds, conclusionIds, ai, ah, ao, wi, wo);
        backPropagate(wordIds, hiddenIds, conclusionIds, ai, ah, ao, wi, wo);
    }

    private void backPropagate(Set<Integer> wordIds,
                               Set<Integer> hiddenIds,
                               Set<Integer> conclusionIds,
                               Map<Integer, Double> ai,
                               Map<Integer, Double> ah,
                               Map<Integer, Double> ao,
                               Map<Integer, Map<Integer, Double>> wi,
                               Map<Integer, Map<Integer, Double>> wo) {

        double N = 0.5;
        Map<Integer, Double> outputDeltas = new FastMap<Integer, Double>();
        Map<Integer, Double> hiddenDeltas = new FastMap<Integer, Double>();
        Map<Integer, Double> targets = new FastMap<Integer, Double>();
        for (int k : conclusionIds) {
            targets.put(k, 1.0);
        }

        for (int k : conclusionIds) {
            double error = targets.get(k) - ao.get(k);
            outputDeltas.put(k, dtanh(ao.get(k)) * error);
        }

        for (int j : hiddenIds) {
            double error = 0.0;
            for (int k : conclusionIds) {
                error = error + outputDeltas.get(k) * wo.get(j).get(k);
            }
            hiddenDeltas.put(j, dtanh(ah.get(j)) * error);
        }

        for (int j : hiddenIds) {
            for (int k : conclusionIds) {
                double change = outputDeltas.get(k) * ah.get(j);
                wo.get(j).put(k, wo.get(j).get(k) + N * change);
            }
        }

        for (int i : wordIds) {
            for (int j : hiddenIds) {
                double change = hiddenDeltas.get(j) * ai.get(i);
                wi.get(i).put(j, wi.get(i).get(j) + N * change);
            }
        }

        for (int i : wordIds) {
            for (int j : hiddenIds) {
                Map<Integer, Synapse> backList = getBackList(j);
                addGraphNodes(backList, i, j, wi.get(i).get(j));
            }
        }

        for (int j : hiddenIds) {
            for (int k : conclusionIds) {
                Map<Integer, Synapse> backList = getBackList(j);
                addGraphNodes(backList, j, k, wo.get(j).get(k));
            }
        }
    }

    Map<Integer, Double> feedForward(Set<Integer> wordIds,
                                     Set<Integer> hiddenIds,
                                     Set<Integer> conclusionIds,
                                     Map<Integer, Double> ai,
                                     Map<Integer, Double> ah,
                                     Map<Integer, Double> ao,
                                     Map<Integer, Map<Integer, Double>> wi,
                                     Map<Integer, Map<Integer, Double>> wo) {
        for (int i : wordIds) {
            ai.put(i, 1.0);
        }
        for (int j : hiddenIds) {
            double sum = 0.0;
            for (int i : wordIds) {
                Double d = wi.get(i).get(j);
                sum = sum + (d != null ? ai.get(i) * d : 0.0);
            }
            ah.put(j, Math.tanh(sum));
        }

        for (int k : conclusionIds) {
            double sum = 0.0;
            for (int j : hiddenIds) {
                Map<Integer, Double> m = wo.get(j);
                if (m != null) {
                    Double d = m.get(k);
                    sum = sum + (d != null ? ah.get(j) * d : 0.0);
                }
            }
            ao.put(k, Math.tanh(sum));
        }
        return ao;
    }

    private double dtanh(double y) {
        return 1.0 - y * y;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void setupNetwork(Set<Integer> wordIds,
                              Set<Integer> hiddenIds,
                              Set<Integer> conclusionIds,
                              Map<Integer, Double> ai,
                              Map<Integer, Double> ah,
                              Map<Integer, Double> ao,
                              Map<Integer, Map<Integer, Double>> wi,
                              Map<Integer, Map<Integer, Double>> wo) {
        /*
        for (int wordId : wordIds) {

            ai.put(wordId, 1.0);
        }
        */
        for (int wordId : hiddenIds) {
            ah.put(wordId, 0.0);
        }
        for (int wordId : conclusionIds) {
            ao.put(wordId, 0.0);
        }
        // now assign weights
        double defaultStrength = 1.0 / wordIds.size();
        for (int wordId : wordIds) {
            Map<Integer, Double> map = new FastMap<Integer, Double>();
            wi.put(wordId, map);
            for (int hiddenId : hiddenIds) {
                //System.out.printf("setup for %d, %d\n", wordId, hiddenId);
                map.put(hiddenId, getStrength(wordId, hiddenId, defaultStrength));
            }
        }
        for (int wordId : hiddenIds) {
            Map<Integer, Double> map = wo.get(wordId);
            if (map == null) {
                map = new FastMap<Integer, Double>();
                wo.put(wordId, map);
            }
            for (int conclusionId : conclusionIds) {
                map.put(conclusionId, getStrength(wordId, conclusionId, 0.1));
            }
        }
    }

    private List<String> getWords(String corpus) {
        List<String> words = new FastList<String>();
        words.addAll(getWordLister().getUniqueWords(corpus));
        return words;
    }

    private List<Integer> getWordIds(List<String> words) {
        List<Integer> wordIds = new FastList<Integer>();
        for (String word : words) {
            wordIds.add(getResolver().getIdForKey(word));
        }
        return wordIds;
    }

    private Set<Integer> getUniqueWordIds(List<Integer> wordIds) {
        Set<Integer> uniqueWordIds = new FastSet<Integer>();
        uniqueWordIds.addAll(wordIds);
        return uniqueWordIds;
    }

    double getStrength(int source, int destination, double defaultVal) {
        Map<Integer, Synapse> synapses = synapsesForward.get(source);
        if (synapses == null) {
            return defaultVal;
        }
        Synapse synapse = synapses.get(destination);
        if (synapse == null) {
            return defaultVal;
        } else {
            return synapse.getWeight();
        }
    }

    private Map<Integer, Synapse> getBackList(int idForHiddenNode) {
        Map<Integer, Synapse> backList = synapsesBackward.get(idForHiddenNode);
        if (backList == null) {
            backList = new FastMap<Integer, Synapse>();
            synapsesBackward.put(idForHiddenNode, backList);
        }
        return backList;
    }

    private String buildHiddenKey(List<String> words) {
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append("_").append(resolver.getIdForKey(word));
        }
        return sb.toString();
    }

    private void addGraphNodes(int source, int destination) {
        if (source == destination) {
            //throw new IllegalArgumentException("source and destination are the same: "+source+", "+destination);
            return;
        }
        Map<Integer, Synapse> map = synapsesForward.get(source);
        if (map == null) {
            map = new FastMap<Integer, Synapse>();
            synapsesForward.put(source, map);
        }
        Synapse synapse = map.get(destination);
        if (synapse == null) {
            synapse = new Synapse(source, destination, 0.1, false);
            map.put(source, synapse);
        }

        map = synapsesBackward.get(destination);
        if (map == null) {
            map = new FastMap<Integer, Synapse>();
            synapsesBackward.put(destination, map);
        }

        synapse = map.get(source);
        if (synapse == null) {
            synapse = new Synapse(source, destination, 0.1, true);
            map.put(source, synapse);
        }
    }

    Set<Integer> getAllHiddenIds(Set<Integer> conclusionIds, Set<Integer> wordIds) {
        Set<Integer> ids = new FastSet<Integer>();
        for (Integer id : synapsesForward.keySet()) {
            if (wordIds.contains(id)) {
                for (Synapse synapse : synapsesForward.get(id).values()) {
                    ids.add(synapse.getDestination());
                }
            }
        }
        for (Integer id : synapsesBackward.keySet()) {
            if (conclusionIds.contains(id)) {
                for (Synapse synapse : synapsesBackward.get(id).values()) {
                    ids.add(synapse.getDestination());
                }
            }
        }
        return ids;
    }

    private void addGraphNodes(Map<Integer, Synapse> backList, int source, int destination, double strength) {
        if (source == destination) {
            //throw new IllegalArgumentException("source and destination are the same: "+source+", "+destination);
            return;
        }
        Map<Integer, Synapse> synapses = synapsesForward.get(source);
        if (synapses == null) {
            synapses = new FastMap<Integer, Synapse>();
            synapsesForward.put(source, synapses);
        }
        Synapse synapse = synapses.get(destination);
        if (synapse == null) {
            // we set strength OUTSIDE the conditional...
            synapse = new Synapse(source, destination, 0.0, true);
            synapses.put(destination, synapse);
        }
        synapse.setWeight(strength);
        synapse = backList.get(source);
        if (synapse == null) {
            synapse = new Synapse(source, destination, 0.0, false);
            backList.put(source, synapse);
        }
        synapse.setWeight(strength);
    }

    public Map<String, Double> analyze(String conclusions, String corpus) {
        List<String> words = getWords(corpus);
        Collections.sort(words);

        Set<Integer> wordIds = getUniqueWordIds(getWordIds(words));
        List<String> conclusionSet = getWords(conclusions);
        Set<Integer> conclusionIds = getUniqueWordIds(getWordIds(conclusionSet));
        int idForHiddenNode = resolver.getIdForKey(buildHiddenKey(words));

        // first let's build the structure for id -> hidden
        // this means back and forth.
        Map<Integer, Synapse> backList = getBackList(idForHiddenNode);

        double strength = 1.0 / words.size();
        for (String word : words) {
            int id = resolver.getIdForKey(word);
            addGraphNodes(backList, id, idForHiddenNode, strength);
        }

        for (int k : conclusionIds) {
            // now let's do the hidden node -> output mapping, which is MUCH simpler.
            addGraphNodes(idForHiddenNode, k);
        }
        // the graph is complete.
        // Now we apply feed forward and back propagation.
        Set<Integer> hiddenIds = getAllHiddenIds(conclusionIds, wordIds);

        Map<Integer, Double> ai = new FastMap<Integer, Double>();
        Map<Integer, Double> ah = new FastMap<Integer, Double>();
        Map<Integer, Double> ao = new FastMap<Integer, Double>();

        Map<Integer, Map<Integer, Double>> wi = new FastMap<Integer, Map<Integer, Double>>();
        Map<Integer, Map<Integer, Double>> wo = new FastMap<Integer, Map<Integer, Double>>();

        setupNetwork(wordIds, hiddenIds, conclusionIds, ai, ah, ao, wi, wo);
        Map<Integer, Double> forwards = feedForward(wordIds, hiddenIds, conclusionIds, ai, ah, ao, wi, wo);
        Map<String, Double> result = new FastMap<String, Double>();
        for (int k : forwards.keySet()) {
            String key = resolver.getKey(k);
            result.put(key, forwards.get(k));
        }
        return result;
    }
}
