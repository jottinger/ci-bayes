package com.enigmastation.dao.gigaspaces;

/**
 * User: joeo
 * Date: 12/16/10
 * Time: 10:35 AM
 * <p/>
 * Copyright
 */

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Visibility;
import com.enigmastation.neuralnet.service.NeuralNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:/ci-bayes-context.xml", "classpath:/gstest.xml"})
public class NeuralNetServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    NeuralNetService service;
    @Autowired
    NeuronDAO neuronDAO;
    /* grr unable to get the getNeuronById() to fail in this code base!!! */
    @Test
    public void testFindById() {
        assertTrue(neuronDAO.toString().contains("gigaspaces"));
        assertTrue(service.getNeuronDAO().toString().contains("gigaspaces"));
        Neuron n=new Neuron();
        n.setPayload("foo");
        n.setVisibility(Visibility.VISIBLE);
        neuronDAO.write(n);
        assertNotNull(n.getId());
        String id=n.getId();
        Neuron neuron=service.getNeuronById(id);
        assertNotNull(neuron);
        assertEquals(neuron.getId(), id);
        assertEquals(neuron, n);
    }
}
