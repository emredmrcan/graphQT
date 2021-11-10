package org.thesis.graphQT.gremlin;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.List;
import java.util.Set;

public class TinkerGraphCreator {

    public static Graph graph() {
        Configuration conf = getNumberIdManagerConfiguration();
        return TinkerGraph.open(conf);
    }

    private static Configuration getNumberIdManagerConfiguration() {
        Configuration conf = new BaseConfiguration();
        conf.setProperty("gremlin.tinkergraph.vertexIdManager", TinkerGraph.DefaultIdManager.LONG.name());
        conf.setProperty("gremlin.tinkergraph.edgeIdManager", TinkerGraph.DefaultIdManager.LONG.name());
        conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager", TinkerGraph.DefaultIdManager.LONG.name());
        return conf;
    }

    public static TinkerGraph createUniversity() {
        TinkerGraph g = (TinkerGraph) graph();
        generateUniversity(g);
        return g;
    }

    public static void generateUniversity(final TinkerGraph g) {
        Vertex deu = g.addVertex(new Object[]{T.id, 1, T.label, "university", "name", "Dokuz Eylul University", "abbreviation", "DEU", "foundation", 1997});
        Vertex unige = g.addVertex(new Object[]{T.id, 2, T.label, "university", "name", "University of Genoa", "abbreviation", "UNIGE", "foundation", 1481});
        Vertex turkey = g.addVertex(new Object[]{T.id, 3, T.label, "country", "name", "TURKEY", "region", List.of("Ege", "Marmara", "Akdeniz")});
        Vertex italy = g.addVertex(new Object[]{T.id, 4, T.label, "country", "name", "ITALY", "region", List.of("Liguria", "Lombardia")});
        deu.addEdge("located_in", turkey);
        unige.addEdge("located_in", italy);
        italy.addEdge("connection", turkey, new Object[]{T.id, 5, "distance", 2985});
        italy.addEdge("connection", turkey, new Object[]{T.id, 6, "distance", 3000});
    }

    public static void addBSBMIndexes(TinkerGraph graph) {
        final Set<String> vertexIndexKeys = Set.of("ProductPropertyTextual_1", "ProductPropertyTextual_2", "ProductPropertyTextual_3",
                "ProductPropertyNumeric_1", "ProductPropertyNumeric_2", "ProductPropertyNumeric_4",
                "review", "reviewerID", "productID",
                "type", "Rating_2", "label_n");

        vertexIndexKeys.forEach(k -> graph.createIndex(k, Vertex.class));
        //graph.getIndexedKeys(Vertex.class).forEach(System.out::println);
    }
}
