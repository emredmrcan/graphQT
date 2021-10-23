package org.thesis.graphQT.cypher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.Record;

import java.io.IOException;
import java.util.List;

import static org.thesis.graphQT.ResourceHelper.loadCypherQuery;

public class CypherQueryExecutionTest extends Neo4jConnectionHelper {

    private final String prefix = "query";
    private Neo4jConnector connector;

    @Before
    public void setUp() {
        connector = new Neo4jConnector(northwindConfig());
    }

    @Test
    public void query4() throws IOException {
        int number = 4;
        final String query = loadCypherQuery(prefix, number);
        final List<Record> records = connector.queryRunner().apply(query);
        Assert.assertEquals(77, records.size());

    }
}
