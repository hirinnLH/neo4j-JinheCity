package com.ecnu.neo4j.util;

import org.neo4j.driver.*;

public class DB {

    private static final String DEFAULT_URL = "bolt://localhost:7687";

    private static final String DEFAULT_DATABASE = "neo4j";

    private static final String DEFAULT_USER = "neo4j";

    private static final String DEFAULT_PASS = "123456";

    private static Driver driver;

    public DB(Driver driver) {
        this.driver = driver;
    }

    public static Session conn() {
        driver = GraphDatabase.driver( DEFAULT_URL, AuthTokens.basic( DEFAULT_USER, DEFAULT_PASS ) );
        return driver.session();
    }


    public static void close() throws Exception {
        driver.close();
    }
}