package com.enigmastation.classifier.springframework;

import org.springframework.beans.factory.FactoryBean;
import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.impl.FisherClassifierImpl;

public class FisherClassifierFactory implements FactoryBean {
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
