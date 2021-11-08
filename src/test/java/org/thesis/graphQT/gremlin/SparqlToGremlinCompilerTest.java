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

package org.thesis.graphQT.gremlin;

import org.apache.commons.io.FileUtils;
import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.translator.GroovyTranslator;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.thesis.graphQT.ResourceHelper.loadBSBMSparqlQuery;
import static org.thesis.graphQT.ResourceHelper.loadSparqlQuery;
import static org.thesis.graphQT.gremlin.SparqlToGremlinCompiler.convertToGremlinTraversal;

public class SparqlToGremlinCompilerTest extends TinkerGraphCreator {
    private final static String NORTH_WIND_GRAPHML = "src/test/resources/graphs/northwind.graphml";
    private final static String BSBM_GRAPHML_1000 = "src/test/resources/graphs/bsbm_scale_1000.graphml";

    private final String prefix = "query";
    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        graph = graph();
        g = graph.traversal();
        //g.io(NORTH_WIND_GRAPHML).with(IO.reader, IO.graphml).read().iterate();
        g.io(BSBM_GRAPHML_1000).with(IO.reader, IO.graphml).read().iterate();
    }

    @Test
    public void transformAndWrite() throws Exception {
        int number = 4;
        GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, loadSparqlQuery(prefix, number));
        String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        final File file = new File("src/test/resources/queries/sparql/gremlin/" + prefix + number + ".gremlin");
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile(file, query, StandardCharsets.UTF_8);

    }

    @Test
    public void transformAndWriteBSBM() throws Exception {
        int number = 2;
        GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, loadBSBMSparqlQuery(prefix, number));
        String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        final File file = new File("src/test/resources/queries/sparql/bsbmgremlin/" + prefix + number + ".gremlin");
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile(file, query, StandardCharsets.UTF_8);
    }

    @Test
    public void customQueryToGremlin() throws Exception {
        System.out.println("Query1");
        String inputQuery = "SELECT  (COUNT (DISTINCT(?product)) as ?total)\n" +
                "WHERE {\n" +
                "              ?a v:type \"review\" .\n" +
                "              ?a e:edge ?product .\n" +
                "}";
        GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, inputQuery);
        String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
        System.out.println("-----------------------------------");
        System.out.println("Query2");
        inputQuery = "SELECT ?property {\n" +
                "    ?a v:ProductPropertyTextual_1 ?property.\n" +
                "    ?a v:productID ?pid.\n" +
                "    FILTER( ?pid = 343 ).\n" +
                "}";
        t = convertToGremlinTraversal(this.graph, inputQuery);
        query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
        System.out.println("-----------------------------------");
        System.out.println("Query3");
        inputQuery = "SELECT  ?a ?review ?r1\n" +
                "WHERE {\n" +
                "    ?a v:reviewerID ?rid.\n" +
                "    ?a e:edge ?review .\n" +
                "    ?review v:Rating_1 ?r1.\n" +
                "    FILTER ( ?rid = 86).\n" +
                "}";
        t = convertToGremlinTraversal(this.graph, inputQuery);
        query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
        System.out.println("-----------------------------------");
        System.out.println("Query4");
        inputQuery = "SELECT ?pid ?property1 ?property2\n" +
                "WHERE {\n" +
                "    ?a v:productID ?pid .\n" +
                "    ?a v:ProductPropertyNumeric_1 ?property1 .\n" +
                "    ?a v:ProductPropertyNumeric_2 ?property2 .\n" +
                "    FILTER ( ?property1 = 1 && ?property2 >100 ).\n" +
                "}";
        t = convertToGremlinTraversal(this.graph, inputQuery);
        query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
        System.out.println("-----------------------------------");
        System.out.println("Query5");
        inputQuery = "SELECT (COUNT(?reviewer) as ?total)\n" +
                "WHERE {\n" +
                "      ?reviewer v:type \"reviewer\".\n" +
                "      ?reviewer v:country ?country.\n" +
                "} GROUP BY(?country)";
        t = convertToGremlinTraversal(this.graph, inputQuery);
        query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
        System.out.println("-----------------------------------");
        System.out.println("Query6");
//        inputQuery = "";
//        t = convertToGremlinTraversal(this.graph, inputQuery);
//        query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
//        System.out.println(query);

    }

    @Test
    public void execute() throws Exception {
        System.out.println("Query1");
        g.V().match(
                __.as("a").values("type").is("review"),
                __.as("a").out("edge").as("product")
        ).dedup("product").toList().forEach(System.out::println);
        System.out.println("----------------------------------------");
        System.out.println("Query2");
        g.V().match(
                __.as("a").values("ProductPropertyTextual_1").as("property"),
                __.as("a").values("productID").as("pid"),
                __.where(__.as("pid").is(P.eq((int) 343)))
        ).select("property").toList().forEach(System.out::println);
        System.out.println("----------------------------------------");
        System.out.println("Query3");
        g.V().match(
                __.as("a").values("reviewerID").as("rid"),
                __.as("a").out("edge").as("review"),
                __.as("review").values("Rating_1").as("r1"),
                __.where(__.as("rid").is(P.eq((int) 86)))
        ).select("a", "review", "r1").toList().forEach(System.out::println);

        System.out.println("----------------------------------------");
        System.out.println("Query4");
        g.V().match(
                __.as("a").values("productID").as("pid"),
                __.as("a").values("ProductPropertyNumeric_1").as("property1"),
                __.as("a").values("ProductPropertyNumeric_2").as("property2"),
                __.where(__.and(__.as("property1").is(P.eq((int) 1)),
                        __.as("property2").is(P.gt((int) 100))))
        ).select("pid", "property1", "property2").toList().forEach(System.out::println);

        System.out.println("----------------------------------------");
        System.out.println("Query5");
        g.V().match(
                __.as("reviewer").values("type").is("reviewer"),
                __.as("reviewer").values("country").as("country")
        ).select("country").groupCount().toList().forEach(System.out::println);

        System.out.println("----------------------------------------");
        System.out.println("Query6");

        System.out.println("----------------------------------------");
        System.out.println("Query7");
    }

}