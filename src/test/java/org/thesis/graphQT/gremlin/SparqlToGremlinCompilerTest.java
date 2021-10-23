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
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.translator.GroovyTranslator;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.thesis.graphQT.ResourceHelper.loadSparqlQuery;
import static org.thesis.graphQT.gremlin.SparqlToGremlinCompiler.convertToGremlinTraversal;

public class SparqlToGremlinCompilerTest extends TinkerGraphCreator {
    private final static String NORTH_WIND_GRAPHML = "src/test/resources/graphs/northwind.graphml";

    private final String prefix = "query";
    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        graph = graph();
        g = graph.traversal();
        g.io(NORTH_WIND_GRAPHML).with(IO.reader, IO.graphml).read().iterate();
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

}