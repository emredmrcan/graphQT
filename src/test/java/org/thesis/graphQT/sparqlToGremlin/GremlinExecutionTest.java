package org.thesis.graphQT.sparqlToGremlin;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.junit.Before;
import org.junit.Test;
import org.thesis.graphQT.gremlin.TinkerGraphCreator;

import java.util.concurrent.Callable;

import static org.apache.tinkerpop.gremlin.process.traversal.P.*;

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
        Callable<GraphTraversal> callable = () -> g.V().match(__.as("a").values("type").is("review"), __.as("a").out().as("product")).dedup("product").count();
        calculateExecutionTimeForSingleQuery(1, callable);

        callable = () -> g.V().match(__.as("a").values("ProductPropertyTextual_1").as("property"), __.as("a").values("productID").as("pid"), __.where(__.as("pid").is(P.eq((int) 343)))).select("property");
        calculateExecutionTimeForSingleQuery(2, callable);

        callable = () -> g.V().match(__.as("a").values("reviewerID").as("rid"), __.as("a").out().as("review"), __.as("review").values("Rating_2").as("r1"), __.where(__.as("rid").is(P.eq((int) 86)))).select("a", "review", "r1");
        calculateExecutionTimeForSingleQuery(3, callable);

        callable = () -> g.V().match(__.as("a").values("productID").as("pid"), __.as("a").values("ProductPropertyNumeric_1").as("property1"), __.as("a").values("ProductPropertyNumeric_2").as("property2"), __.where(__.and(__.as("property1").is(P.eq((int) 1)), __.as("property2").is(P.gt((int) 100))))).select("pid", "property1", "property2");
        calculateExecutionTimeForSingleQuery(4, callable);

        callable = () -> g.V().match(__.as("reviewer").values("type").is("reviewer"), __.as("reviewer").values("country").as("country")).select("country").groupCount();
        calculateExecutionTimeForSingleQuery(5, callable);

        callable = () -> g.V().match(__.as("product").values("ProductPropertyNumeric_4").as("propn4"))
                .select("propn4").groupCount().order(Scope.local).by(Column.keys, Order.asc)
                .range(Scope.local, 0L, 20L);
        calculateExecutionTimeForSingleQuery(6, callable);

        callable = () -> g.V().match(__.as("review").values("type").is("review"), __.as("review").out().as("product"), __.as("product").values("productID").as("pid")).select("pid").groupCount().order(Scope.local).by(Column.values, Order.desc).range(Scope.local, 0L, 10L);
        calculateExecutionTimeForSingleQuery(7, callable);

        callable = () -> g.V().match(
                __.as("a").values("type").is("review"),
                __.as("a").values("Rating_2").as("rating"),
                __.as("a").out().as("product"),
                __.as("product").values("productID").as("pid"),
                __.where(__.as("pid").is(P.eq((int) 343)))
        ).select("rating").range(0L, 2L);
        calculateExecutionTimeForSingleQuery(8, callable);

        callable = () -> g.V().match(
                __.as("reviewer").values("type").is("reviewer"),
                __.as("reviewer").out().as("review"),
                __.as("review").out().as("product")
        ).select("reviewer").groupCount()
                .order(Scope.local).by(Column.values, Order.desc)
                .range(Scope.local, 0L, 10L);
        calculateExecutionTimeForSingleQuery(9, callable);

        callable = () -> g.V().match(
                __.as("producer").values("type").is("producer"),
                __.as("producer").values("label_n").as("plabel"),
                __.as("producer").out().as("product"),
                __.as("product").values("type").is("product"),
                __.as("product").values("productID").as("pid"),
                __.as("product").values("label_n").as("label"),
                __.as("product").values("comment").as("comment"),
                __.as("product").values("ProductPropertyTextual_1").as("proptext1"),
                __.as("product").values("ProductPropertyTextual_2").as("proptext2"),
                __.as("product").values("ProductPropertyTextual_3").as("proptext3"),
                __.as("product").values("ProductPropertyNumeric_1").as("propnum1"),
                __.as("product").values("ProductPropertyNumeric_2").as("propnum2"),
                __.as("product").out().as("pfeature"),
                __.as("pfeature").values("type").is("product_feature"),
                __.as("pfeature").values("label_n").as("flabel"),
                __.where(__.as("pid").is(P.eq((int) 343)))
        ).select("plabel", "label", "flabel", "proptext1", "proptext2", "proptext3", "propnum1", "propnum2", "comment")
                .range(0L, 1L);
        calculateExecutionTimeForSingleQuery(10, callable);

        callable = () -> g.V().union(__.match(__.as("a").values("productID").as("pid"), __.as("a").values("label_n").as("label"), __.as("a").values("ProductPropertyNumeric_1").as("ppn1"), __.where(__.as("pid").is(P.eq((int) 343)))), __.match(__.as("a").values("productID").as("pid"), __.as("a").values("label_n").as("label"), __.as("a").values("ProductPropertyNumeric_1").as("ppn1"), __.where(__.as("pid").is(P.eq((int) 350))))).select("pid", "label", "ppn1");
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
        g.V().has("ProductPropertyNumeric_2", gt(100))
                .has("ProductPropertyNumeric_1", eq(1))
                .project("n.productID", "n.ProductPropertyNumeric_1", "n.ProductPropertyNumeric_2")
                .by(__.choose(__.values("productID"), __.values("productID"), __.constant("  cypher.null")))
                .by(__.choose(neq("  cypher.null"),
                        __.choose(__.values("ProductPropertyNumeric_1"), __.values("ProductPropertyNumeric_1"),
                                __.constant("  cypher.null"))))
                .by(__.choose(neq("  cypher.null"), __.choose(__.values("ProductPropertyNumeric_2"),
                        __.values("ProductPropertyNumeric_2"), __.constant("  cypher.null"))))


                .toList().forEach(System.out::println);
    }
}
