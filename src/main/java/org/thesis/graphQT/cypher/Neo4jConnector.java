package org.thesis.graphQT.cypher;

import org.neo4j.driver.*;

import java.util.List;
import java.util.function.Function;

public class Neo4jConnector implements AutoCloseable {

    private final Driver driver;

    public Neo4jConnector(Neo4jConnectionConfiguration conf) {
        this.driver = GraphDatabase.driver(conf.getUri(), AuthTokens.basic(conf.getUsername(), conf.getPassword()));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public Function<String, List<Record>> queryRunner() {
        return (query) -> {
            try (Session session = driver.session()) {
                return session.run(query).list();
            }
        };
    }
}
