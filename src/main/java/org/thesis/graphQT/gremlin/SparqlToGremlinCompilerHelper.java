package org.thesis.graphQT.gremlin;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class SparqlToGremlinCompilerHelper {

    public static Graph graph() {
        TinkerGraph g = getTinkerGraphWithNumberManager();
        generate(g);
        return g;
    }

    public static void generate(final TinkerGraph g) {
        Vertex jim = g.addVertex(T.id, 1, T.label, "student", "name", "jim black", "age", 25, "gender","male");

        Vertex deu = g.addVertex(T.id, 2, T.label, "university", "name", "DEU");

        Vertex cs = g.addVertex(T.id, 3, T.label, "department", "name", "computer science", "code", "cs");

        jim.addEdge("member_of", deu, T.id, 4, "acceptance_date", "01/01/2021");
        deu.addEdge("has",cs,T.id,5);
    }

    private static TinkerGraph getTinkerGraphWithNumberManager() {
        Configuration conf = getNumberIdManagerConfiguration();
        return TinkerGraph.open(conf);
    }

    private static Configuration getNumberIdManagerConfiguration() {
        Configuration conf = new BaseConfiguration();
        conf.setProperty("gremlin.tinkergraph.vertexIdManager", TinkerGraph.DefaultIdManager.INTEGER.name());
        conf.setProperty("gremlin.tinkergraph.edgeIdManager", TinkerGraph.DefaultIdManager.INTEGER.name());
        conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager", TinkerGraph.DefaultIdManager.LONG.name());
        return conf;
    }
}
