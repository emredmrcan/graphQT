package org.thesis.graphQT.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.translator.GroovyTranslator;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.thesis.graphQT.ResourceHelper.loadUniversityDomainSparqlQuery;
import static org.thesis.graphQT.gremlin.SparqlToGremlinCompiler.convertToGremlinTraversal;

public class universityDomainSparqlToCypher extends TinkerGraphCreator {
    private final String prefix = "query";
    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        graph = createUniversity();
        g = graph.traversal();
    }

    @Test
    public void queryTranslations() throws IOException {
        int totalQueries = 3;
        for (int i = 1; i <= totalQueries; i++) {
            translateAndPrint(i);
        }
    }

    @Test
    public void execution() throws IOException {
        final List<Object> objects = g.V().match(
                __.as("s").hasLabel("university"),
                __.as("s").values("abbreviation").as("abbreviation"),
                __.where(__.as("abbreviation").is(P.eq("DEU"))))
                .select("s").toList();
        objects.forEach(System.out::println);
        System.out.println("-----------------------------------");
        final List<Object> objects1 = g.V().match(
                __.as("a").hasLabel("country"),
                __.as("a").values("name").as("b"))
                .dedup("b").select("b").toList();

        objects1.forEach(System.out::println);
        System.out.println("-----------------------------------");
    }

    @Test
    public void name() {
        g.V().match(__.as("a").hasLabel("university"), __.as("a").out("located_in").as("b"), __.as("b").values("name").is("ITALY"), __.as("a").values("foundation").as("c"), __.where(__.as("c").is(P.lt((int) 1500)))).order().by(__.select("a"), Order.asc).by(__.select("c"))
                .toList().forEach(System.out::println);
        g.V().match(
                __.as("a").hasLabel("university"),
                __.as("a").out("located_in").as("b"),
                __.as("b").values("name").is("ITALY"),
                __.as("a").values("foundation").as("c"),
                __.where(__.as("c").is(P.lt((int) 1500))))
                .order()
                .by(__.select("a"), Order.asc)
                .by(__.select("c"));

    }

    private void translateAndPrint(int number) throws IOException {
        final String sparqlQuery = loadUniversityDomainSparqlQuery(prefix, number);
        System.out.println(sparqlQuery);
        GraphTraversal<Vertex, ?> t = convertToGremlinTraversal(this.graph, sparqlQuery);
        String query = GroovyTranslator.of("g").translate(t.asAdmin().getBytecode()).getScript();
        System.out.println(query);
    }
}
