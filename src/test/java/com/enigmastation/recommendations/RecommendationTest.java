package com.enigmastation.recommendations;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import static org.testng.Assert.*;
import com.enigmastation.recommendations.data.Critic;
import com.enigmastation.recommendations.item.DistanceRecommendationImpl;
import com.enigmastation.recommendations.item.RecommendationImpl;
import com.enigmastation.recommendations.item.PearsonCorrelationCoefficientImpl;
import com.enigmastation.collections.NestedDictionaryStringStringDouble;
import com.enigmastation.collections.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jun 19, 2008
 * Time: 12:16:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationTest {
    Map<String, Critic> critics = null;
    NestedDictionaryStringStringDouble nd = null;

    @BeforeTest(groups = {"fulltest", "normal"})
    public void setup() {
        critics = new HashMap<String, Critic>();
        critics.put("Lisa Rose",
                new Critic("Lisa Rose",
                        "Lady in the Water", 2.5,
                        "Snakes on a Plane", 3.5,
                        "Just My Luck", 3.0,
                        "Superman Returns", 3.5,
                        "You, Me, and Dupree", 2.5,
                        "The Night Listener", 3.0));
        critics.put("Gene Seymour",
                new Critic("Gene Seymour",
                        "Lady in the Water", 3.0,
                        "Snakes on a Plane", 3.5,
                        "Just My Luck", 1.5,
                        "Superman Returns", 5.0,
                        "You, Me, and Dupree", 3.5,
                        "The Night Listener", 3.0));
        critics.put("Claudia Pulig",
                new Critic("Claudia Pulig",
                        "Snakes on a Plane", 3.5,
                        "Just My Luck", 3.0,
                        "Superman Returns", 4.0,
                        "You, Me, and Dupree", 2.5,
                        "The Night Listener", 4.5));
        critics.put("Michael Phillips",
                new Critic("Michael Phillips",
                        "Lady in the Water", 2.5,
                        "Snakes on a Plane", 3.0,
                        "Superman Returns", 3.5,
                        "The Night Listener", 4.0));
        critics.put("Mick LaSalle",
                new Critic("Mick LaSalle",
                        "Lady in the Water", 3.0,
                        "Snakes on a Plane", 4.0,
                        "Just My Luck", 2.0,
                        "Superman Returns", 3.0,
                        "You, Me, and Dupree", 2.0,
                        "The Night Listener", 3.0));
        critics.put("Jack Matthews",
                new Critic("Jack Matthews",
                        "Lady in the Water", 3.0,
                        "Snakes on a Plane", 4.0,
                        "Superman Returns", 5.0,
                        "You, Me, and Dupree", 3.5,
                        "The Night Listener", 3.0));
        critics.put("Toby",
                new Critic("Toby",
                        "Snakes on a Plane", 4.5,
                        "Superman Returns", 4.0,
                        "You, Me, and Dupree", 1.0));
    }

    @AfterTest(groups = {"fulltest", "normal"})
    public void tearDown() {
        critics = null;
        nd = null;
    }

    @Test(groups = {"fulltest", "normal"})
    public void TestCriticsSetup() {
        assertEquals(critics.get("Lisa Rose").getScore("Lady in the Water"), 2.5);
        critics.get("Toby").setScore("Snakes on a Plane", 4.5);
        assertEquals(critics.get("Toby").getScore("Snakes on a Plane"), 4.5);

    }

    @Test(groups = {"fulltest", "normal"})
    public void TestRecommendationDistance() {
        RecommendationImpl recommendationImpl = new DistanceRecommendationImpl();

        assertEquals(recommendationImpl.getDistance(criticsToDictionary(critics), "Lisa Rose", "Gene Seymour"),
                0.148148148148, 0.0000001);
    }

    @Test(groups = {"fulltest", "normal"})
    public void TestPearsonDistance() {
        RecommendationImpl recommendationImpl = new PearsonCorrelationCoefficientImpl();

        assertEquals(recommendationImpl.getDistance(criticsToDictionary(critics), "Lisa Rose", "Gene Seymour"),
                0.396059017191, 0.0000001);
    }

    @Test(groups = {"fulltest", "normal"})
    public void TestMostSimilar() {
        RecommendationImpl recommendationImpl = new PearsonCorrelationCoefficientImpl();
        List<Tuple> matches = recommendationImpl.getTopMatches(criticsToDictionary(critics),
                "Toby", 3);
        // should return Lisa - who's most similar to toby
        // then Mick LaSalle
        // then Claudia Pulig
        assertEquals(matches.get(0).getKey(), "Lisa Rose");
        assertEquals(matches.get(1).getKey(), "Mick LaSalle");
        assertEquals(matches.get(2).getKey(), "Claudia Pulig");
    }

    @Test(groups = {"fulltest", "normal"})
    public void TestGetRecommendation() {
        RecommendationImpl recommendationImpl = new PearsonCorrelationCoefficientImpl();
        NestedDictionaryStringStringDouble nd = criticsToDictionary(critics);
        List<Tuple> matches = recommendationImpl.getRecommendations(nd, "Toby");
        assertEquals(matches.get(0).getKey(), "The Night Listener");
        assertEquals(matches.get(1).getKey(), "Lady in the Water");
        assertEquals(matches.get(2).getKey(), "Just My Luck");

    }

    private NestedDictionaryStringStringDouble criticsToDictionary(Map<String, Critic> critics) {
        NestedDictionaryStringStringDouble nd = new NestedDictionaryStringStringDouble();
        for (String topKey : critics.keySet()) {
            for (String movie : critics.get(topKey).getScores().keySet()) {
                nd.save(topKey, movie, critics.get(topKey).getScore(movie));
            }
        }
        return nd;
    }
}
