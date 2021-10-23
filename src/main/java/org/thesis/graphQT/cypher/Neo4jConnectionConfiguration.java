package org.thesis.graphQT.cypher;

import lombok.Getter;

@Getter
public class Neo4jConnectionConfiguration {
    public final String uri;
    public final String username;
    public final String password;

    public Neo4jConnectionConfiguration(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }
}
