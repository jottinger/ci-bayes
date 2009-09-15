package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.impl.SynapseMap;

public interface NetworkModelFactory {
    SynapseMap buildForwardMap();

    SynapseMap buildBackwardMap();
}
