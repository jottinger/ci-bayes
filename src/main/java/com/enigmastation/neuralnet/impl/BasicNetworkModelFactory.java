package com.enigmastation.neuralnet.impl;

import com.enigmastation.neuralnet.NetworkModelFactory;

public class BasicNetworkModelFactory implements NetworkModelFactory {
    public SynapseMap buildForwardMap() {
        return new SynapseMap();
    }

    public SynapseMap buildBackwardMap() {
        return new SynapseMap();
    }
}
