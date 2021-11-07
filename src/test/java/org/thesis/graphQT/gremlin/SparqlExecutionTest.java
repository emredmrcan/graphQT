package org.thesis.graphQT.gremlin;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;

public class SparqlExecutionTest {

    static String urlDB = "jdbc:virtuoso://localhost:1111";
    Statement st;

    static String defaultGraphIRI = "http://example.com";

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
    public void name2() throws SQLException {
        String query = "sparql PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>\n" +
                "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "\n" +
                "SELECT DISTINCT ?product ?label\n" +
                "from <http://example.com> WHERE { \n" +
                " ?product rdfs:label ?label .\n" +
                " ?product bsbm:productPropertyTextual1 ?b  \n" +
                "}";
        System.currentTimeMillis();
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        ResultSet rs = st.executeQuery(query);
        prnRs(rs);
        endTime = System.currentTimeMillis();
        System.out.println(
                "time taken for convertToGremlinTraversal Function : " + (endTime - startTime) + " mili seconds");

        query = "sparql PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>\n" +
                "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "\n" +
                "SELECT DISTINCT ?product ?label\n" +
                "from <http://example2.com> WHERE { \n" +
                " ?product rdfs:label ?label .\n" +
                " ?product bsbm:productPropertyTextual1 ?b  \n" +
                "}";

        startTime = System.currentTimeMillis();
        rs = st.executeQuery(query);
        prnRs(rs);
        endTime = System.currentTimeMillis();
        System.out.println(
                "time taken for convertToGremlinTraversal Function : " + (endTime - startTime) + " mili seconds");
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
