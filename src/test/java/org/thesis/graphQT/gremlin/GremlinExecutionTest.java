package org.thesis.graphQT.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.P.neq;

public class GremlinExecutionTest extends TinkerGraphCreator {

    private final static String NORTH_WIND_GRAPHML = "src/test/resources/graphs/northwind.graphml";

    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        graph = graph();
        g = graph.traversal();
        g.io(NORTH_WIND_GRAPHML).with(IO.reader, IO.graphml).read().iterate();
    }

    @Test
    public void gremlinQuery4() {
        List<Map<String, Object>> maps = g.V().match(
                __.as("a").values("productName").as("b"),
                __.as("a").values("unitsInStock").as("c"),
                __.where(
                        __.as("c").is(P.gt(20))
                ))
                .dedup("b", "c").select("b", "c").toList();

        System.out.println(maps);
    }

    @Test
    public void gremlinCypherTranslationQuery4() {
        List<Map<String, Object>> maps = g.V()
                .as("n")
                .where(
                        __.and(__.select("n")
                                        .choose(neq("  cypher.null"),
                                                __.choose(__.values("productName"),
                                                        __.values("productName"),
                                                        __.constant("  cypher.null")))
                                        .choose(__.is(neq("  cypher.null")),
                                                __.constant(true),
                                                __.constant(false))
                                        .is(neq("  cypher.null"))
                                        .is(eq(true)),
                                __.select("n")
                                        .choose(neq("  cypher.null"),
                                                __.choose(__.values("unitsInStock"),
                                                        __.values("unitsInStock"),
                                                        __.constant("  cypher.null")))
                                        .choose(__.is(neq("  cypher.null")),
                                                __.constant(true),
                                                __.constant(false))
                                        .is(neq("  cypher.null"))
                                        .is(eq(true))))
                .select("n").project("n.productName", "n.unitsInStock")
                .by(__.choose(neq("  cypher.null"),
                        __.choose(__.values("productName"),
                                __.values("productName"),
                                __.constant("  cypher.null"))))
                .by(__.choose(neq("  cypher.null"),
                        __.choose(__.values("unitsInStock"),
                                __.values("unitsInStock"),
                                __.constant("  cypher.null"))))
                .toList();

        Assert.assertEquals(77, maps.size());
        System.out.println(maps);
    }
}
