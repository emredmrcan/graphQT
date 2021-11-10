package org.thesis.graphQT.cypherToGremlin;

import org.thesis.graphQT.cypher.Neo4jConnectionConfiguration;

public class Neo4jConnectionHelper {

    public static Neo4jConnectionConfiguration northwindConfig() {
        return new Neo4jConnectionConfiguration("bolt://localhost:7687", "neo4j", "Northwind");
    }

    public static Neo4jConnectionConfiguration bsbm1000Config() {
        return new Neo4jConnectionConfiguration("bolt://localhost:7687", "neo4j", "BSBM_1000");
    }
}
