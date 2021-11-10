package org.thesis.graphQT.cypherToGremlin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.Record;
import org.thesis.graphQT.cypher.Neo4jConnector;

import java.util.List;

public class Neo4jConnectionTest extends Neo4jConnectionHelper {
    private Neo4jConnector connector;

    @Before
    public void setUp() {
        connector = new Neo4jConnector(northwindConfig());
    }

    @Test
    public void connectionTest() {
        final List<Record> records = connector.queryRunner().apply("Match (n) RETURN n");
        System.out.println(records.size());
        Assert.assertFalse(records.isEmpty());
    }
}
