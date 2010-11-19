package com.enigmastation.dao;

import org.testng.annotations.Test;

public class Db4ODAOTest {
    @Test
    public void testOperations() {
        Db4OTestDAO dao=new Db4OTestDAO();
        TestEntity e=dao.build();
        e.setText("now is the time");
        System.out.println("removing "+dao.take(e));
        System.out.println("e: "+e);
        dao.write(e);
        System.out.println("e: "+e);
        System.out.flush();
    }
}
