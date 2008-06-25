package com.enigmastation.neuralnet.dao;

import com.enigmastation.neuralnet.NeuralNetDAO;
import com.enigmastation.neuralnet.impl.dao.openspaces.OpenSpacesNeuralNetDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OpenSpacesDAOTest {
    ApplicationContext ctx;
    NeuralNetDAO dao;

    @BeforeTest(groups = "normal")
    public void setup() {
        ctx = new ClassPathXmlApplicationContext("/spring.xml");
        dao = (NeuralNetDAO) ctx.getBean("neuralnetdao");
    }

    @Test(groups = "normal")
    public void testSpringConfig() {
        assertNotNull(ctx);
        assertNotNull(dao);
        assertNotNull(((OpenSpacesNeuralNetDAO) dao).getSpace());
    }

    @Test(groups = "normal")
    public void testResolver() {
        dao.addKey("wWorld", 101);
        assertEquals(dao.getId("wWorld"), 101);
        dao.addKey("wBank");
        assertEquals(dao.getId("wBank"), 102);
        assertEquals(dao.getKey(101), "wWorld");
        dao.addKey("uWorldBank", 201);
        assertEquals(dao.getId("uWorldBank"), 201);
    }
}
