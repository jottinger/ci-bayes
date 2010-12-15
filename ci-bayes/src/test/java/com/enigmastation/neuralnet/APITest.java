package com.enigmastation.neuralnet;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.SynapseDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Pair;
import com.enigmastation.neuralnet.impl.Perceptron;
import com.enigmastation.neuralnet.service.NeuralNetService;
import com.enigmastation.resolvers.Resolver;
import com.enigmastation.resolvers.impl.MemoryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * User: joeo
 * Date: 11/20/10
 * Time: 7:25 AM
 * <p/>
 * Copyright
 */
@ContextConfiguration(locations = {"/ci-bayes-context.xml"})
public class APITest extends AbstractTestNGSpringContextTests {
    @Autowired
    NeuralNetService service;
    @Autowired
    SynapseDAO synapseDAO;
    @Autowired
    NeuronDAO neuronDAO;
    @Autowired
    Perceptron neuralNetwork;
    Resolver resolver = new MemoryResolver();

    @BeforeMethod
    public void resetNetwork() {
        neuralNetwork.reset();
    }

    @Test
    public void testAPI() {
        List<Pair<Neuron, Double>> n = neuralNetwork.getOutputs("how do I use JQuery with Java");
        System.out.println("result from new network query: " + n);
        int[] results = {resolver.addKey("use DWR instead"),
                resolver.addKey("JAX-RS"),
                resolver.addKey("SerfJ"),
                resolver.addKey("JQuery"),
        };
        System.out.println("resolver results: " + Arrays.toString(results));
        for (int idx = 0; idx < 30; idx++) {
            neuralNetwork.train("I want to use javascript with java", "r" + String.valueOf(results[1]));
            neuralNetwork.train("I want to call a java method from a javascript", "r" + String.valueOf(results[0]));
            neuralNetwork.train("I want to use REST with Java", "r" + String.valueOf(results[2]));
            neuralNetwork.train("I want to use JQuery with Java", "r" + String.valueOf(results[3]));
            neuralNetwork.train("I would like to be able to use J2EE security roles from javascript", "r" + String.valueOf(results[1]));
            neuralNetwork.train("I would like to be able to use J2EE security with javascript", "r" + String.valueOf(results[0]));
            neuralNetwork.train("I want to use javascript with java", "r" + String.valueOf(results[1]));
        }
        n = neuralNetwork.getOutputs("how do I use JQuery with Java");
        for (Pair p : n) {
            System.out.println(p);
        }
    }
}
