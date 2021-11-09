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
        //language=SQL
        String query = "sparql prefix bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
                "prefix rev: <http://purl.org/stuff/rev#>\n" +
                "\n" +
                "Select  (count  (distinct (?pid)) as ?totalprods)\n" +
                "from<http://bsbmScale1000.com>\n" +
                "where\n" +
                "    { ?review bsbm:reviewFor ?product .\n" +
                "    ?product rdfs:label ?pid.\n" +
                "    " +
                "}";
        System.currentTimeMillis();
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        ResultSet rs = st.executeQuery(query);
        prnRs(rs);
        endTime = System.currentTimeMillis();
        //System.out.println("time taken for convertToGremlinTraversal Function : " + (endTime - startTime) + " mili seconds");
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
