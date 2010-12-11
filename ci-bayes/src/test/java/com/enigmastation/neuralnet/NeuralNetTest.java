package com.enigmastation.neuralnet;

import com.enigmastation.extractors.WordLister;
import com.enigmastation.extractors.impl.StemmingWordLister;
import com.enigmastation.neuralnet.dao.NeuronDAO;
import com.enigmastation.neuralnet.dao.SynapseDAO;
import com.enigmastation.neuralnet.model.Neuron;
import com.enigmastation.neuralnet.model.Synapse;
import com.enigmastation.neuralnet.service.NeuralNetService;
import com.enigmastation.resolvers.Resolver;
import com.enigmastation.resolvers.impl.MemoryResolverFactory;
import com.gigaspaces.simpledao.dao.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: 11/3/10
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"/neuralnet.xml", "classpath:/com/gigaspaces/simpledao/dao-context.xml", "classpath:/com/gigaspaces/simpledao/gigaspaces-context.xml"})
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
        Resolver resolver = new MemoryResolverFactory().build();
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
        showArray(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        showArray(synapseDAO.readMultiple(new Synapse()));
        System.out.println("neurons connected to wBank: ");
        showCollection(neuralNetwork.getHiddenNeuronsForInput("wBank"));
        neuralNetwork.trainNaive("wRiver wBank", "uRiver");
        System.out.println("neurons connected to wBank: ");
        showCollection(neuralNetwork.getHiddenNeuronsForInput("wBank"));
        System.out.println("outputs:");
        showCollection(neuralNetwork.getOutputs("wWorld wBank", new Actor() {
            void handle(List<Pair<Neuron, Double>> list, Neuron n, double weight) {
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
        showArray(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        showArray(synapseDAO.readMultiple(new Synapse()));
    }

    @Test
    public void testTraining2() {
        neuralNetwork.reset();
        neuralNetwork.trainNaive("wWorld wBank", "uWorldBank uRiver uEarth");
        System.out.println("--------");
        System.out.println("neurons: ");
        showArray(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        showArray(synapseDAO.readMultiple(new Synapse()));
        for (String s : new String[]{"a", "b", "c", "d", "e"}) {
            neuralNetwork.train("wWorld wBank", "uWorldBank");
            neuralNetwork.train("wRiver wBank", "uRiver");
            neuralNetwork.train("wWorld", "uEarth");
        }

        System.out.println("neurons: ");
        showArray(neuronDAO.readMultiple(new Neuron()));
        System.out.println("synapses: ");
        showArray(synapseDAO.readMultiple(new Synapse()));

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

    private void showArray(Object[] objects) {
        if (objects == null) {
            System.out.println("null");
            return;
        }
        for (Object o : objects) {
            System.out.println("   " + o);
        }
    }
}
