package com.enigmastation.classifier.springframework;

import org.springframework.beans.factory.FactoryBean;
import com.enigmastation.extractors.impl.SimpleWordLister;
import com.enigmastation.extractors.WordLister;

public final class WordListerFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new SimpleWordLister();
    }

    public Class getObjectType() {
        return WordLister.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
