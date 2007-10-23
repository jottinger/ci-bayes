package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.WordLister;
import javolution.util.FastMap;

import java.util.Map;
import java.util.Set;

public class FisherClassifierImpl extends NaiveClassifierImpl implements FisherClassifier {
    Map<String, Double> minimums=new FastMap<String, Double>();

    public void setMinimum(String category, double minimum) {
        minimums.put(category, minimum);
    }
    
    public double getMinimum(String category) {
        if(minimums.containsKey(category)) {
            return minimums.get(category);
        }
        return 0.0;
    }

    @Override
    protected double fprob(String feature, String cat) {
        double clf=super.fprob(feature, cat);
        if(clf==0.0) {
            return 0.0;
        }
        double freqsum=0.0;

        for(String c:categories()) {
            double d=super.fprob(feature, c);
            freqsum=freqsum+d;
        }

        return clf/freqsum;
    }

    double fisherprob(String item, String cat) {
        double p=1.0;
        Set<String> features=extractor.getUniqueWords(item);
        for(String f:features) {
            p*=getWeightedProbability(f,cat);
        }
        double fscore=-2*Math.log(p);
        return invchi2(fscore, features.size()*2);
    }

    public double getFisherProbability(String item, String category) {
        return fisherprob(item, category);
    }

    private double invchi2(double chi, int df) {
        double m=chi/2.0;
        double sum=Math.exp(-m);
        double term=sum;
        for(int i=1;i<df/2;i++) {
            term*=m/i;
            sum+=term;
        }
        return Math.min(sum,1.0);
    }

    public FisherClassifierImpl(WordLister w) {
        super(w);
    }

    public FisherClassifierImpl() {
        super();    
    }

    @Override
    public String getClassification(String item, String defaultCat) {
        String best=defaultCat;
        double max=0.0;
        for(String c:categories()) {
            double p=fisherprob(item, c);
            if(p>getMinimum(c) && p>max) {
                best=c;
                max=p;
            }
        }
        return best;
    }
}
