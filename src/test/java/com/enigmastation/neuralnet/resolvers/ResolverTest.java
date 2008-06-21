package com.enigmastation.neuralnet.resolvers;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import com.enigmastation.neuralnet.Resolver;
import com.enigmastation.neuralnet.impl.resolvers.StringResolver;

public class ResolverTest {
    @Test(groups="normal")
    public void resolverTest() {
        Resolver<String> resolver=new StringResolver();
        resolver.addKey("wWorld",101);
        assertEquals(resolver.getId("wWorld"), 101);
        resolver.addKey("wBank");
        assertEquals(resolver.getId("wBank"), 102);
        assertEquals(resolver.getKey(101), "wWorld");
        resolver.addKey("uWorldBank", 201);
        assertEquals(resolver.getId("uWorldBank"), 201);
    }
}
