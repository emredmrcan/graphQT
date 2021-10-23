package org.thesis.graphQT.cypher;

public class Neo4jConnectionHelper {

    public static Neo4jConnectionConfiguration northwindConfig() {
        return new Neo4jConnectionConfiguration("bolt://localhost:7687", "neo4j", "Northwind");
    }
}
