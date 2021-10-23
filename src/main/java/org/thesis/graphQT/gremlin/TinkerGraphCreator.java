package org.thesis.graphQT.gremlin;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

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
}
