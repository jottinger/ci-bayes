package com.enigmastation.neuralnet.service.impl;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.SynapseDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Synapse;
import com.enigmastation.dao.model.Visibility;
import com.enigmastation.neuralnet.service.NeuralNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NeuralNetServiceImpl implements NeuralNetService {
    @Autowired
    NeuronDAO neuronDAO;
    @Autowired
    SynapseDAO synapseDAO;

    public Neuron getNeuronById(String id) {
        if(id==null) {
            throw new IllegalArgumentException();
        }
        return neuronDAO.readById(id);
    }

    public Neuron getNeuron(String payload, Visibility visibility) {
        return getNeuron(payload, visibility, false);
    }

    @Transactional
    public void setStrength(Neuron from, Neuron to, double strength) {
        checkArguments(from, to, Visibility.VISIBLE);
        Synapse template = new Synapse();
        template.setFrom(from.getId());
        template.setTo(to.getId());
        synapseDAO.take(template);
        template.setStrength(strength);
        synapseDAO.write(template);
    }

    public Set<Synapse> getSynapsesFrom(Neuron neuron) {
        if (neuron == null) {
            return new HashSet<Synapse>(0);
        }
        //System.out.println("getSynapsesFrom("+neuron+")");
        Set<Synapse> synapses = new HashSet<Synapse>();
        Synapse template = new Synapse();
        template.setFrom(neuron.getId());
        List<Synapse> synapseList = synapseDAO.readMultiple(template);

        synapses.addAll(synapseList);

        return synapses;
    }

    @Transactional
    public double getStrength(Neuron from, Neuron to, Visibility visibility) {
        checkArguments(from, to, visibility);
        Synapse template = new Synapse();
        template.setFrom(from.getId());
        template.setTo(to.getId());
        Synapse r = synapseDAO.read(template);
        if (r == null) {
            return visibility.getStrength();
        }
        return r.getStrength();
    }

    private void checkArguments(Neuron from, Neuron to, Visibility visibility) {
        if(to==null || from==null || visibility==null) {
            throw new IllegalArgumentException(from+","+to+","+visibility);
        }
    }

    public void reset() {
        synapseDAO.takeMultiple(new Synapse());
        neuronDAO.takeMultiple(new Neuron());
    }

    public SynapseDAO getSynapseDAO() {
        return synapseDAO;
    }

    public NeuronDAO getNeuronDAO() {
        return neuronDAO;
    }

    @Transactional
    public Neuron getNeuron(String payload, Visibility visibility, boolean createIfNecessary) {
        Neuron template = new Neuron();
        template.setPayload(payload);
        template.setVisibility(visibility);
        Neuron neuron = neuronDAO.read(template);
        if (neuron == null && createIfNecessary) {
            neuron = neuronDAO.write(template);
        }
        return neuron;
    }
}
