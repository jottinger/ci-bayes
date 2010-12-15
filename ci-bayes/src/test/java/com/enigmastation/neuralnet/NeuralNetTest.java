package com.enigmastation.neuralnet;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.SynapseDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Pair;
import com.enigmastation.dao.model.Synapse;
import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.impl.StemmingWordLister;
import com.enigmastation.neuralnet.impl.Perceptron;
import com.enigmastation.neuralnet.service.NeuralNetService;
import com.enigmastation.resolvers.Resolver;
import com.enigmastation.resolvers.impl.MemoryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertNotNull;

@ContextConfiguration(locations = {"/ci-bayes-context.xml"})
public class NeuralNetTest extends AbstractTestNGSpringContextTests {
    @Autowired
    NeuralNetService service;
    @Autowired
    SynapseDAO synapseDAO;
    @Autowired
    NeuronDAO neuronDAO;
    @Autowired
    Perceptron neuralNetwork;

    @Test
    public void testConversion() {
        WordLister wordLister = new StemmingWordLister();
        Resolver resolver = new MemoryResolver();
        System.out.println(wordLister.getUniqueWords("stem stemming stemmed"));
        resolver.getIdForKey("stem");
    }

    @Test
    public void testConfiguration() {
        assertNotNull(service);
        assertNotNull(neuralNetwork);
    }

    @Test
    public void testWorld() {
        neuralNetwork.reset();

        neuralNetwork.trainNaive("wWorld wBank", "uWorldBank uRiver uEarth");
        System.out.println("neurons: ");
        this.showCollection(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        this.showCollection(synapseDAO.readMultiple(new Synapse()));
        System.out.println("neurons connected to wBank: ");
        showCollection(neuralNetwork.getHiddenNeuronsForInput("wBank"));
        neuralNetwork.trainNaive("wRiver wBank", "uRiver");
        System.out.println("neurons connected to wBank: ");
        showCollection(neuralNetwork.getHiddenNeuronsForInput("wBank"));
        System.out.println("outputs:");
        showCollection(neuralNetwork.getOutputs("wWorld wBank", new Actor() {
            public void handle(List<Pair<Neuron, Double>> list, Neuron n, double weight) {
                list.add(new Pair<Neuron, Double>(n, weight));
            }
        }));
    }

    @Test
    public void testNullNetwork() {
        neuralNetwork.reset();
        dumpNetwork();
        neuralNetwork.getOutputs("one two three four");
        dumpNetwork();
    }

    private void dumpNetwork() {
        System.out.println("neurons: ");
        this.showCollection(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        this.showCollection(synapseDAO.readMultiple(new Synapse()));
    }

    @Test
    public void testTraining2() {
        neuralNetwork.reset();
        neuralNetwork.trainNaive("wWorld wBank", "uWorldBank uRiver uEarth");
        System.out.println("--------");
        System.out.println("neurons: ");
        this.showCollection(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        this.showCollection(synapseDAO.readMultiple(new Synapse()));
        for (String s : new String[]{"a", "b", "c", "d", "e"}) {
            neuralNetwork.train("wWorld wBank", "uWorldBank");
            neuralNetwork.train("wRiver wBank", "uRiver");
            neuralNetwork.train("wWorld", "uEarth");
        }

        System.out.println("neurons: ");
        this.showCollection(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        this.showCollection(synapseDAO.readMultiple(new Synapse()));

        System.out.println("OUTPUT of wWorld wBank");
        showCollection(neuralNetwork.getOutputs("wWorld wBank"));
        System.out.println("OUTPUT of wRiver wBank");
        showCollection(neuralNetwork.getOutputs("wRiver wBank"));
        System.out.println("OUTPUT of wWorld");
        showCollection(neuralNetwork.getOutputs("wWorld"));
        System.out.println("OUTPUT of wBank");
        showCollection(neuralNetwork.getOutputs("wBank"));

    }

    private void showCollection(Collection<?> objects) {
        if (objects == null) {
            System.out.println("null");
            return;
        }
        for (Object o : objects) {
            System.out.println("   " + o);
        }
    }

    private void showCollection(Object[] objects) {
        if (objects == null) {
            System.out.println("null");
            return;
        }
        for (Object o : objects) {
            System.out.println("   " + o);
        }
    }
}
