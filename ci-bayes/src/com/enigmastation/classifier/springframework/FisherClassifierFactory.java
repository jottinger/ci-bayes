package com.enigmastation.classifier.springframework;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.impl.FisherClassifierImpl;
import org.springframework.beans.factory.FactoryBean;

public final class FisherClassifierFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new FisherClassifierImpl();
    }

    public Class getObjectType() {
        return FisherClassifier.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
