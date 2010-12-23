package com.enigmastation.neuralnet.service;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.SynapseDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Synapse;
import com.enigmastation.dao.model.Visibility;

import java.util.Set;

public interface NeuralNetService {
    Neuron getNeuron(String payload, Visibility visibility, boolean createIfNecessary);

    Neuron getNeuronById(String id);

    Neuron getNeuron(String payload, Visibility visibility);

    void setStrength(Neuron from, Neuron to, double strength);

    Set<Synapse> getSynapsesFrom(Neuron neuron);

    double getStrength(Neuron from, Neuron to, Visibility v);

    void reset();

    SynapseDAO getSynapseDAO();

    NeuronDAO getNeuronDAO();
}
