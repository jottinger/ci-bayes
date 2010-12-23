package com.enigmastation.neuralnet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * User: joeo
 * Date: 12/15/10
 * Time: 12:17 PM
 * <p/>
 * Copyright
 */
@ContextConfiguration(locations = {"/ci-bayes-context.xml"})
public class DZoneTest extends AbstractTestNGSpringContextTests {
    @Autowired
    NeuralNetwork network;

    @Test
    public void testConfig() {
        assertNotNull(network);
    }

    @Test
    public void testTrain() {
        network.train("cats. Cats are nice. They're feline animals who excel as they hunt vermin.", "t00001");
        network.train("dogs. Dogs are canine and related to wolves. They dislike cats, usually.", "t00002");
        for(Object o:network.getOutputs("cats are nice")) {
            System.out.println(o);
        }
    }
}
