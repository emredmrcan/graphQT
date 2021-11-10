package org.thesis.graphQT.cypherToGremlin;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.Record;
import org.thesis.graphQT.cypher.Neo4jConnector;

import java.util.List;

import static org.thesis.graphQT.ResourceHelper.loadBSBMCypherQuery;

public class CypherExecutionTest extends Neo4jConnectionHelper {

    private final String prefix = "query";
    private final int totalQuery = 11;

    private Neo4jConnector connector;

    @Before
    public void setUp() {
        //connector = new Neo4jConnector(northwindConfig());
        connector = new Neo4jConnector(bsbm1000Config());
    }

    @Test
    public void calculateExecutionTimeForAllQueries() throws Exception {
        for (int qID = 1; qID < totalQuery + 1; qID++) {
            //for (int qID = totalQuery; qID > 0; qID--) {
            calculateExecutionTime(qID);
        }
    }

    @Test
    public void calculateWarmExecutionTimeForQueries() throws Exception {
        int warmUpRuns = 20;

        //WarmUp
        for (int i = 0; i < warmUpRuns; i++) {
            calculateExecutionTimeForAllQueries();
        }
        //Actual Run
        System.out.println("Actual Run");
        calculateExecutionTimeForAllQueries();
    }

    @Test
    public void calculateExecutionTimeForSingleQuery() throws Exception {
        int qID = 1;
        calculateExecutionTime(qID);
    }

    private void calculateExecutionTime(int qID) throws Exception {
        final String query = loadBSBMCypherQuery(prefix, qID);
        calculateExecutionTime(qID, query);
    }

    public void calculateExecutionTime(int qID, String query) throws Exception {
        long startTime, endTime;
        startTime = System.nanoTime();
        final List<Record> records = connector.queryRunner().apply(query);
        endTime = System.nanoTime();
        final double total = (endTime - startTime) / 1e6;
        System.out.println(qID + ": " + total);
        System.out.println("------------------");
    }
}
