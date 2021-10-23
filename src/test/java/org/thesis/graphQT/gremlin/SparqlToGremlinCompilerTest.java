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

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.translator.GroovyTranslator;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;

import static org.thesis.graphQT.ResourceHelper.loadSparqlQuery;
import static org.thesis.graphQT.gremlin.SparqlToGremlinCompiler.convertToGremlinTraversal;

public class SparqlToGremlinCompilerTest extends SparqlToGremlinCompilerHelper {

    private Graph thesisGraph;
    private GraphTraversalSource tg;

    @Before
    public void setUp() {
        thesisGraph = graph();
        tg = thesisGraph.traversal();
    }

    @Test
    public void thesisQuery1() throws Exception {
        GraphTraversal<Vertex, ?> thesis = convertToGremlinTraversal(this.thesisGraph, loadSparqlQuery("thesis", 1));
        String query = GroovyTranslator.of("g").translate(thesis.asAdmin().getBytecode()).getScript();
        System.out.println(query);
    }

}