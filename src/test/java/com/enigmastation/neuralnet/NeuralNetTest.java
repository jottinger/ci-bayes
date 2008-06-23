package com.enigmastation.neuralnet;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import com.enigmastation.neuralnet.impl.resolvers.StringResolver;
import com.enigmastation.neuralnet.impl.neuralnets.Perceptron;
import com.enigmastation.neuralnet.impl.neuralnets.BaseNeuralNet;


public class NeuralNetTest {
    Resolver<String> resolver;
    BaseNeuralNet<String> net;
    @BeforeTest(groups="normal")
    public void setup() {
        resolver=new StringResolver();
        net=new Perceptron<String>(resolver);
        resolver.addKey("wWorld", 101);
        resolver.addKey("wRiver", 102);
        resolver.addKey("wBank", 103);
        resolver.addKey("uWorldBank", 201);
        resolver.addKey("uRiver", 202);
        resolver.addKey("uEarth", 203);
    }
    @Test(groups="normal")
    public void testNeuralNet() {
        net.generateHiddenNode(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"});
        net.dump(0);
        net.dump(1);
        System.out.println(net.getResult(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
    }
}
