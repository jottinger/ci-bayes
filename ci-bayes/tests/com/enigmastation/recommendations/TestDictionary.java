package com.enigmastation.recommendations;

import com.enigmastation.recommendations.item.DistanceRecommendationImpl;
import com.enigmastation.recommendations.item.PearsonCorrelationCoefficientImpl;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class TestDictionary {
    @Test(groups = {"all"})
    public void testDictionary() {
        NestedDictionary critics = buildDictionary();
        System.out.println(critics.size());
        assertEquals(critics.size(), 7);
    }

    public void testComparator() {
        NestedDictionary critics = buildDictionary();
        System.out.println(critics.containsKey("lisa rose"));
    }

    public void testSimDistance() {
        NestedDictionary critics = buildDictionary();
        Recommendation r = new DistanceRecommendationImpl();
        System.out.println(r.getDistance(critics, "Lisa Rose", "Gene Seymour"));
    }

    public void testPearsonDistance() {
        NestedDictionary critics = buildDictionary();
        Recommendation r = new PearsonCorrelationCoefficientImpl();
        System.out.println(r.getDistance(critics, "Lisa Rose", "Gene Seymour"));
    }

    public void testTopMatches() {
        NestedDictionary critics = buildDictionary();
        Recommendation r = new PearsonCorrelationCoefficientImpl();
        System.out.println(r.getTopMatches(critics, "Toby", 3));
    }

    public void testRecommendations() {
        NestedDictionary critics = buildDictionary();
        Recommendation r = new PearsonCorrelationCoefficientImpl();
        System.out.println(r.getRecommendations(critics, "Toby"));
        r = new DistanceRecommendationImpl();
        System.out.println(r.getRecommendations(critics, "Toby"));
    }

    public static NestedDictionary buildDictionary() {
        NestedDictionary critics = new NestedDictionary();
        critics.save("Lisa Rose", "Lady in the Water", 2.5);
        critics.save("Lisa Rose", "Snakes on a Plane", 3.5);
        critics.save("Lisa Rose", "Just My Luck", 3.0);
        critics.save("Lisa Rose", "Superman Returns", 3.5);
        critics.save("Lisa Rose", "You, Me, and Dupree", 2.5);
        critics.save("Lisa Rose", "The Night Listener", 3.0);

        critics.save("Gene Seymour", "Lady in the Water", 3.0);
        critics.save("Gene Seymour", "Snakes on a Plane", 3.5);
        critics.save("Gene Seymour", "Just My Luck", 1.5);
        critics.save("Gene Seymour", "Superman Returns", 5.0);
        critics.save("Gene Seymour", "The Night Listener", 3.0);
        critics.save("Gene Seymour", "You, Me, and Dupree", 3.5);

        critics.save("Michael Phillips", "Lady in the Water", 2.5);
        critics.save("Michael Phillips", "Snakes on a Plane", 3.0);
        critics.save("Michael Phillips", "Superman Returns", 3.5);
        critics.save("Michael Phillips", "The Night Listener", 4.0);

        critics.save("Claudia Puig", "Snakes on a Plane", 3.5);
        critics.save("Claudia Puig", "Just My Luck", 3.0);
        critics.save("Claudia Puig", "The Night Listener", 4.5);
        critics.save("Claudia Puig", "Superman Returns", 4.0);
        critics.save("Claudia Puig", "You, Me, and Dupree", 2.5);

        critics.save("Mick LaSalle", "Lady in the Water", 3.0);
        critics.save("Mick LaSalle", "Snakes on a Plane", 4.0);
        critics.save("Mick LaSalle", "Just My Luck", 2.0);
        critics.save("Mick LaSalle", "Superman Returns", 3.0);
        critics.save("Mick LaSalle", "The Night Listener", 3.0);
        critics.save("Mick LaSalle", "You, Me, and Dupree", 2.0);

        critics.save("Jack Matthews", "Lady in the Water", 3.0);
        critics.save("Jack Matthews", "Snakes on a Plane", 4.0);
        critics.save("Jack Matthews", "The Night Listener", 3.0);
        critics.save("Jack Matthews", "Superman Returns", 5.0);
        critics.save("Jack Matthews", "You, Me, and Dupree", 3.5);

        critics.save("Toby", "Snakes on a Plane", 4.5);
        critics.save("Toby", "You, Me, and Dupree", 1.0);
        critics.save("toby", "Superman Returns", 4.0);
        return critics;
    }

    public static void main(String[] args) {
        TestDictionary td = new TestDictionary();
        td.testDictionary();
        td.testComparator();
        td.testSimDistance();
        td.testPearsonDistance();
        td.testTopMatches();
        td.testRecommendations();
    }
}
