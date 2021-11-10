package org.thesis.graphQT.cypherToGremlin;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.junit.Before;
import org.junit.Test;
import org.thesis.graphQT.gremlin.TinkerGraphCreator;

import java.util.concurrent.Callable;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.P.neq;

public class GremlinExecutionTest extends TinkerGraphCreator {

    private final static String BSBM_GRAPHML_1000 = "src/test/resources/graphs/bsbm_scale_1000.graphml";
    private final static String BSBM_GRAPHML_10000 = "src/test/resources/graphs/bsbm_scale_10000.graphml";

    private Graph graph;
    private GraphTraversalSource g;

    @Before
    public void setUp() {
        final String selectedGraphML = BSBM_GRAPHML_1000;
        graph = graph();
        g = graph.traversal();
        g.io(selectedGraphML).with(IO.reader, IO.graphml).read().iterate();
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
    public void calculateExecutionTimeForAllQueries() throws Exception {
        Callable<GraphTraversal> callable = null;

        callable = () -> g.V().has("type", eq("review")).outE().inV()
                .dedup().is(neq("  cypher.null"))
                .count().project("count(distinct product)")
                .by(__.identity());
        calculateExecutionTimeForSingleQuery(1, callable);

        callable = () -> g.V().has("productID", eq(343))
                .project("n.ProductPropertyTextual_1").
                        by(__.choose(
                                __.values("ProductPropertyTextual_1"),
                                __.values("ProductPropertyTextual_1"),
                                __.constant("  cypher.null")));
        calculateExecutionTimeForSingleQuery(2, callable);

        callable = () -> g.V().as("a").has("reviewerID", eq(86))
                .outE().inV().as("r")
                .where(
                        __.select("r")
                                .choose(neq("  cypher.null"),
                                        __.choose(
                                                __.values("Rating_2"),
                                                __.values("Rating_2"),
                                                __.constant("  cypher.null"))
                                ).choose(__.is(neq("  cypher.null")), __.constant(true), __.constant(false))
                                .is(neq("  cypher.null"))
                                .is(eq(true))
                ).select("a", "r")
                .project("a.identity", "r.identity", "r.Rating_2")
                .by(__.select("a").choose(neq("  cypher.null"),
                        __.choose(__.values("identity"),
                                __.values("identity"),
                                __.constant("  cypher.null")))
                ).by(__.select("r").choose(neq("  cypher.null"),
                        __.choose(__.values("identity"),
                                __.values("identity"),
                                __.constant("  cypher.null"))))
                .by(__.select("r").choose(neq("  cypher.null"),
                        __.choose(__.values("Rating_2"),
                                __.values("Rating_2"),
                                __.constant("  cypher.null"))));
        calculateExecutionTimeForSingleQuery(3, callable);

        callable = () -> g.V().has("ProductPropertyNumeric_2", P.gt(100))
                .has("ProductPropertyNumeric_1", eq(1))
                .project("n.productID", "n.ProductPropertyNumeric_1", "n.ProductPropertyNumeric_2")
                .by(__.choose(__.values("productID"), __.values("productID"), __.constant("  cypher.null")))
                .by(__.choose(neq("  cypher.null"),
                        __.choose(__.values("ProductPropertyNumeric_1"), __.values("ProductPropertyNumeric_1"),
                                __.constant("  cypher.null"))))
                .by(__.choose(neq("  cypher.null"), __.choose(__.values("ProductPropertyNumeric_2"),
                        __.values("ProductPropertyNumeric_2"), __.constant("  cypher.null"))));
        calculateExecutionTimeForSingleQuery(4, callable);

        callable = () -> g.V().has("type", eq("reviewer")).group().by(__.choose(__.values("country"), __.values("country"), __.constant("  cypher.null"))).by(__.fold().project("a.country", "count(*)").by(__.unfold().choose(neq("  cypher.null"), __.choose(__.values("country"), __.values("country"), __.constant("  cypher.null")))).by(__.unfold().count())).unfold().select(Column.values);
        calculateExecutionTimeForSingleQuery(5, callable);

        callable = () -> g.V().group().by(__.choose(__.values("ProductPropertyNumeric_4"), __.values("ProductPropertyNumeric_4"),
                __.constant("  cypher.null"))).by(__.fold().project("a.ProductPropertyNumeric_4", "count(*)")
                .by(__.unfold().choose(neq("  cypher.null"),
                        __.choose(__.values("ProductPropertyNumeric_4"), __.values("ProductPropertyNumeric_4"),
                                __.constant("  cypher.null")))).by(__.unfold().count()))
                .unfold().select(Column.values).limit(20);
        calculateExecutionTimeForSingleQuery(6, callable);

        callable = () -> g.V().as("review").has("type", eq("review")).outE().inV().as("product")
                .select("product", "review").group().by(__.select("product").choose(__.values("productID"),
                        __.values("productID"), __.constant("  cypher.null")))
                .by(__.fold().project("product.productID", "total").by(__.unfold().select("product")
                        .choose(neq("  cypher.null"), __.choose(__.values("productID"),
                                __.values("productID"), __.constant("  cypher.null"))))
                        .by(__.unfold().select("review").is(neq("  cypher.null")).count())).unfold()
                .select(Column.values).order().by(__.select("total"), Order.desc).limit(10);
        calculateExecutionTimeForSingleQuery(7, callable);

        callable = () -> g.V().as("review").has("type", eq("review")).outE().inV().has("productID", eq(343))
                .select("review").project("review.Rating_2")
                .by(__.choose(__.values("Rating_2"), __.values("Rating_2"), __.constant("  cypher.null")))
                .limit(2);
        calculateExecutionTimeForSingleQuery(8, callable);

        callable = () -> g.V().as("reviewer").has("type", eq("reviewer")).outE().as("  UNNAMED35")
                .inV().outE().as("  UNNAMED48").inV().where(__.select("  UNNAMED35")
                        .where(neq("  UNNAMED48"))).select("reviewer").group().by(__.identity())
                .by(__.fold().project("reviewer", "total").by(__.unfold())
                        .by(__.unfold().count())).unfold()
                .select(Column.values).order().by(__.select("total"), Order.desc).limit(10)
                .project("reviewer", "total").by(__.select("reviewer")
                        .valueMap().with("~tinkerpop.valueMap.tokens")).by(__.select("total").identity());
        calculateExecutionTimeForSingleQuery(9, callable);

        callable = () -> g.V().has("productID", eq(343)).project("n").by(__.valueMap().with("~tinkerpop.valueMap.tokens"));
        calculateExecutionTimeForSingleQuery(10, callable);

        callable = () -> g.inject("  cypher.start")
                .union(__.V().has("productID", eq(343))
                                .project("n.ProductPropertyNumeric_1").by(__.choose(neq("  cypher.null"),
                                __.choose(__.values("ProductPropertyNumeric_1"),
                                        __.values("ProductPropertyNumeric_1"), __.constant("  cypher.null")))),
                        __.V().has("productID", eq(350)).project("n.ProductPropertyNumeric_1")
                                .by(__.choose(neq("  cypher.null"), __.choose(__.values("ProductPropertyNumeric_1"),
                                        __.values("ProductPropertyNumeric_1"), __.constant("  cypher.null")))))
                .dedup();
        calculateExecutionTimeForSingleQuery(11, callable);
    }

    public void calculateExecutionTimeForSingleQuery(int qID, Callable<GraphTraversal> fn) throws Exception {
        long startTime, endTime;
        startTime = System.nanoTime();
        fn.call();
        endTime = System.nanoTime();
        final double total = (endTime - startTime) / 1e6;
        System.out.println(qID + ": " + total);
        System.out.println("------------------");
    }

    @Test
    public void singleQueryTest() {

    }
}
