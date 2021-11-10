package org.thesis.graphQT.sparqlToGremlin;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;

import static org.thesis.graphQT.ResourceHelper.loadOriginalBSBMSparqlQuery;

public class SparqlExecutionTest {

    static String urlDB = "jdbc:virtuoso://localhost:1111";
    Statement st;

    private final int SCALE_FACTOR = 10000;
    private final String prefix = "query";
    private final int totalQuery = 11;

    @Before
    public void setUp() throws Exception {
        Class.forName("virtuoso.jdbc4.Driver");
        Connection conn = DriverManager.getConnection(urlDB, "dba", "dba");
        st = conn.createStatement();
    }

    @Test
    public void name() throws SQLException {
        String query = "sparql SELECT * from <http://example.com> WHERE {?s ?p ?o}";
        ResultSet rs = st.executeQuery(query);
        prnRs(rs);
    }

    @Test
    public void calculateExecutionTime() throws Exception {
        long startTime, endTime;
        for (int qID = 1; qID < totalQuery + 1; qID++) {
            //for (int qID = totalQuery; qID > 0; qID--) {
            final String inputQuery = "sparql " + loadOriginalBSBMSparqlQuery(prefix, qID, SCALE_FACTOR);

            startTime = System.nanoTime();
            ResultSet rs = st.executeQuery(inputQuery);
            countResultSet(rs);
            endTime = System.nanoTime();

            final double total = (endTime - startTime) / 1e6;
            System.out.println(qID + ": " + total);
            System.out.println("------------------");
        }
    }

    @Test
    public void calculateColdExecutionTimeForSingleQuery() throws Exception {
        int qID = 11;
        calculateExecutionTime(qID);
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

    private void calculateExecutionTimeForAllQueries() throws IOException, SQLException {
        for (int qID = 1; qID < totalQuery + 1; qID++) {
            calculateExecutionTime(qID);
        }
    }

    private void calculateExecutionTime(int qID) throws IOException, SQLException {
        long startTime, endTime;
        final String inputQuery = "sparql " + loadOriginalBSBMSparqlQuery(prefix, qID, SCALE_FACTOR);

        startTime = System.nanoTime();
        ResultSet rs = st.executeQuery(inputQuery);
        //countResultSet(rs);
        endTime = System.nanoTime();

        final double total = (endTime - startTime) / 1e6;
        System.out.println(qID + ": " + total);
        System.out.println("------------------");
    }

    private void countResultSet(ResultSet rs) throws SQLException {
        int i = 0;
        while (rs.next()) {
            i++;
        }
        System.out.println("Total result size: " + i);
    }

    void prnRs(ResultSet rs) {
        try {
            ResultSetMetaData rsmd;

            System.out.println(">>>>>>>>");
            rsmd = rs.getMetaData();
            int cnt = rsmd.getColumnCount();

            while (rs.next()) {
                Object o;

                System.out.print("Thread:" + Thread.currentThread().getId() + "  ");
                for (int i = 1; i <= cnt; i++) {
                    o = rs.getObject(i);
                    if (rs.wasNull())
                        System.out.print("<NULL> ");
                    else
                        System.out.print("[" + o + "] ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println(">>>>>>>>");
    }
}
