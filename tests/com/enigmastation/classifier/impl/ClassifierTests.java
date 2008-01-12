package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.Classifier;
import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.NaiveClassifier;
import com.enigmastation.classifier.WordLister;
import com.enigmastation.classifier.persistence.Serializer;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import java.io.IOException;

public class ClassifierTests {
    @Test(groups = {"all"})
    public void testWords() {
        WordLister w=new WordListerImpl();
        assertEquals(w.getUniqueWords("Now is the time - 'now'").size(),3);
    }

    @Test
    public void testIncc() {
        ClassifierImpl impl=new ClassifierImpl(new WordListerImpl());
        impl.incc("foo");
    }
    
    @Test
    public void testInternalFeatureCount() {
        ClassifierImpl impl=new ClassifierImpl(new WordListerImpl());
        impl.train("the quick brown fox jumps over the lazy dog", "good");
        impl.train("make quick money in the online casino", "bad");
        assertEquals(impl.fcount("quick", "good"),1.0,0.1);
        assertEquals(impl.fcount("quick", "bad"),1.0,0.1);
    }

    @Test
    public void testWeightedProbabilityByCategory() {
        Classifier cl=getClassifier();
        assertEquals(cl.getFeatureProbability("quick", "good"),0.666666,0.000001);
        assertEquals(cl.getWeightedProbability("money", "good"),0.25, 0.001);
        
        sampleTrain(cl);
        
        assertEquals(cl.getWeightedProbability("money", "good"), 0.166666,0.00001);

    }

    @Test
    public void testCategoryProbabilities() {
        NaiveClassifier nc=getNaiveClassifier();
        assertEquals(nc.getProbabilityForCategory("quick rabbit", "good"), 0.15624, 0.0001);
        assertEquals(nc.getProbabilityForCategory("quick rabbit", "bad"), 0.05, 0.01);
    }

    @Test
    public void testSerializableSave() throws IOException {
        NaiveClassifier cl=new NaiveClassifierImpl();
        Serializer s=new Serializer();
        sampleTrain(cl);
        s.save(cl);
    }

    @Test(dependsOnMethods = {"testSerializableSave"})
    public void testSerializableLoad() throws IOException, ClassNotFoundException {
        NaiveClassifier cl=new NaiveClassifierImpl();
        Serializer s=new Serializer();
        s.load(cl);
        assertEquals(cl.getProbabilityForCategory("quick rabbit", "good"), 0.15624,0.0001);
        assertEquals(cl.getProbabilityForCategory("quick rabbit", "bad"),0.05, 0.01);
    }

    @Test
    public void testNaiveCategoryAssignment() {
        NaiveClassifier nc=getNaiveClassifier();
        if(!nc.getClassification("quick rabbit", "unknown").equals("good")) {
            throw new RuntimeException("failed getting good");
        }
        if(!nc.getClassification("quick money", "unknown").equals("bad")) {
            throw new RuntimeException("failed getting bad");
        }
        nc.setCategoryThreshold("bad", 3.0);
        if(!nc.getClassification("quick money", "unknown").equals("unknown")) {
            throw new RuntimeException("failed getting unknown");
        }
        for(int i=0;i<9;i++) {
            sampleTrain(nc);
        }
        if(!nc.getClassification("quick money", "unknown").equals("bad")) {
            throw new RuntimeException("failed getting bad");
        }
    }

    @Test
    public void testFisherClassifier() {
        FisherClassifier nc=getFisherClassifier();
        assertEquals(nc.getFeatureProbability("quick", "good"), 0.5714,0.0001);
        assertEquals(nc.getFeatureProbability("money", "bad"), 1.0,0.0001);
        assertEquals(nc.getWeightedProbability("money","bad"),0.75,0.001);
        assertEquals(nc.getFisherProbability("quick rabbit", "good"), 0.7801,0.0001);
        assertEquals(nc.getFisherProbability("quick rabbit", "bad"), 0.3563,0.0001);
        assertEquals(nc.getClassification("quick rabbit", "none"), "good");
        assertEquals(nc.getClassification("quick money", "none"), "bad");
        nc.setMinimum("bad", 0.8);
        assertEquals(nc.getClassification("quick money", "none"), "good");
        nc.setMinimum("good", 0.5);
        assertEquals(nc.getClassification("quick money", "none"), "none");
    }

    Classifier getClassifier() {
        Classifier cl=new ClassifierImpl(new WordListerImpl());
        sampleTrain(cl);
        return cl;
    }
    
    NaiveClassifier getNaiveClassifier() {
        NaiveClassifier nc=new NaiveClassifierImpl(new WordListerImpl());
        sampleTrain(nc);
        return nc;
    }

    FisherClassifier getFisherClassifier() {
        FisherClassifier nc=new FisherClassifierImpl();
        sampleTrain(nc);
        return nc;
    }

    private void sampleTrain(Classifier cl) {
        cl.train("Nobody owns the water.", "good");
        cl.train("the quick rabbit jumps fences", "good");
        cl.train("buy pharmaceuticals now", "bad");
        cl.train("make quick money in the online casino", "bad");
        cl.train("the quick brown fox jumps", "good");
    }
}
