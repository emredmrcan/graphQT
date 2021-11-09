/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.thesis.graphQT.sparqlToGremlin;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.translator.GroovyTranslator;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;
import org.thesis.graphQT.gremlin.TinkerGraphCreator;

import static org.thesis.graphQT.ResourceHelper.loadRewrittenBSBMSparqlQuery;
import static org.thesis.graphQT.gremlin.SparqlToGremlinCompiler.convertToGremlinTraversal;

public class SparqlToGremlinTranslation extends TinkerGraphCreator {
    private final static String NORTH_WIND_GRAPHML = "src/test/resources/graphs/northwind.graphml";
    private final static String BSBM_GRAPHML_1000 = "src/test/resources/graphs/bsbm_scale_1000.graphml";

    private final String prefix = "query";
    private final int totalQuery = 11;
    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        final String selectedGraphML = BSBM_GRAPHML_1000;
        graph = graph();
        g = graph.traversal();
        g.io(selectedGraphML).with(IO.reader, IO.graphml).read().iterate();
    }

    /*@Test
    public void transformAndWrite() throws Exception {
        for (int qID = 1; qID < totalQuery + 1 ; qID++) {
            System.out.println("STARTED: " + qID);
            GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, loadRewrittenBSBMSparqlQuery(prefix, qID));
            String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
            final File file = new File("src/test/resources/queries/sparql/bsbm/sparqlToGremlin/" + prefix + qID + "." + Languages.gremlin.name());
            FileUtils.writeStringToFile(file, query, StandardCharsets.UTF_8);
            System.out.println("FINISHED: " + qID);
            System.out.println("------------------");
        }
    }*/

    @Test
    public void calculateTranslationTime() throws Exception {
        long startTime, endTime;
        double totalTimePerQuery = 0;
        for (int qID = 1; qID < totalQuery + 1; qID++) {
            final String inputQuery = loadRewrittenBSBMSparqlQuery(prefix, qID);
            String outputQuery = "";
            System.out.println(inputQuery);
            for (int i = 0; i < 10; i++) {
                startTime = System.nanoTime();
                GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, inputQuery);
                outputQuery = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
                endTime = System.nanoTime();
                final double total = (endTime - startTime) / 1e6;
                totalTimePerQuery += total;
            }
            System.out.println(outputQuery);
            System.out.println(qID + ": " + totalTimePerQuery / 10);
            System.out.println("------------------");
            totalTimePerQuery = 0;
        }
    }

    @Test
    public void executionTest() throws Exception {
        String input = "SELECT (count(?propn4) as ?total)\n" +
                "WHERE {\n" +
                "    ?product v:ProductPropertyNumeric_4 ?propn4.\n" +
                "} GROUP BY(?propn4) ORDER BY ASC(?propn4) LIMIT 20";
        GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, input);
        String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
    }

    @Test
    public void easyTest() throws Exception {

    }

}