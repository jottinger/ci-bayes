package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.impl.Perceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@ContextConfiguration(locations = {"/ci-bayes-context.xml"})
public class XORTest extends AbstractTestNGSpringContextTests {
    @Autowired
    Perceptron neuralNetwork;

    @Test
    public void testXOR() {
       for(int i=0;i<10; i++) {
           neuralNetwork.train("1true 2or 4false", "5true");
           neuralNetwork.train("1true 2and 4false", "5false");
           neuralNetwork.train("1false 2or 4true", "5true");
           neuralNetwork.train("1false 2and 4false", "5false");
           neuralNetwork.train("1true 2and 3not 4true", "5false");
           neuralNetwork.train("1true 2and 3not 4false", "5true");
           neuralNetwork.train("1false 2and 3not 4true", "5true");
           neuralNetwork.train("1false 2and 3not 4false", "5false");
       }
        assertEquals(neuralNetwork.getOutputs("1true 2or 4false").get(0).getK().getPayload(), "5true");
        assertEquals(neuralNetwork.getOutputs("1true 2and 4false").get(0).getK().getPayload(), "5fals");

        assertEquals(neuralNetwork.getOutputs("1true 2xor 4false").get(0).getK().getPayload(), "5true");
        assertEquals(neuralNetwork.getOutputs("1false 2xor 4false").get(0).getK().getPayload(), "5fals");
        assertEquals(neuralNetwork.getOutputs("1true 2xor 4true").get(0).getK().getPayload(), "5fals");
        assertEquals(neuralNetwork.getOutputs("1false 2xor 4true").get(0).getK().getPayload(), "5true");
    }
}
