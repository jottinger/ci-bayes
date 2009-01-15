package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.impl.neuralnets.Perceptron;
import com.enigmastation.neuralnet.model.Layer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class NeuralNetTestInvalid {
    NeuralNet net;
    ApplicationContext ctx;
    NeuralNetDAO dao;

    @BeforeTest(groups = "normal")
    public void setupSpring() {
        ctx = new ClassPathXmlApplicationContext("/spring.xml");
        dao = (NeuralNetDAO) ctx.getBean("neuralnetdao");
        net = (NeuralNet) ctx.getBean("neuralnet");

        dao.addKey("wWorld", 101);
        dao.addKey("wRiver", 102);
        dao.addKey("wBank", 103);
        dao.addKey("uWorldBank", 201);
        dao.addKey("uRiver", 202);
        dao.addKey("uEarth", 203);
    }

    @Test(groups = "normal")
    public void testNeuralNet() {
        ((Perceptron) net).generateHiddenNode(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"});
        ((Perceptron) net).dump(Layer.TOHIDDEN);
        ((Perceptron) net).dump(Layer.FROMHIDDEN);
        System.out.println(net.getResult(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
        net.trainquery(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}, "uWorldBank");
        System.out.println(net.getResult(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
        /* net.dump(0);
   net.dump(1);
        */
        for (int i = 0; i < 30; i++) {
            net.trainquery(new String[]{"wWorld", "wBank"},
                    new String[]{"uWorldBank", "uRiver", "uEarth"},
                    "uWorldBank");
            net.trainquery(new String[]{"wRiver", "wBank"},
                    new String[]{"uWorldBank", "uRiver", "uEarth"},
                    "uRiver");
            net.trainquery(new String[]{"wWorld"},
                    new String[]{"uWorldBank", "uRiver", "uEarth"},
                    "uEarth");
        }

        System.out.println(net.getResult(new String[]{"wWorld", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
        System.out.println(net.getResult(new String[]{"wRiver", "wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
        System.out.println(net.getResult(new String[]{"wBank"},
                new String[]{"uWorldBank", "uRiver", "uEarth"}));
        /*
        net.dump(0);
        net.dump(1);
        */
    }
}
