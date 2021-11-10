package org.thesis.graphQT;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceHelper {

    public static String loadQuery(final String path) throws IOException {
        final InputStream stream = ResourceHelper.class.getResourceAsStream(path);
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

    public static String loadBSBMCypherQuery(final String prefix, final int number) throws IOException {
        String path = "/queries/bsbm/" + Languages.cypher.name() + "/" + prefix + number + "." + Languages.cypher.name();
        return loadQuery(path);
    }

    public static String loadOriginalBSBMSparqlQuery(String prefix, int number, int scale) throws IOException {
        String path = "/queries/bsbm/originalSparql" + scale + "/" + prefix + number + "." + Languages.sparql.name();
        return loadQuery(path);
    }

    public static String loadRewrittenBSBMSparqlQuery(String prefix, int number) throws IOException {
        String path = "/queries/bsbm/rewrittenSparql/" + prefix + number + "." + Languages.sparql.name();
        return loadQuery(path);
    }

    public static String loadUniversityDomainSparqlQuery(final String prefix, final int number) throws IOException {
        String path = "/queries/" + Languages.sparql.name() + "/sectionSparqlToCypher/" + prefix + number + "." + Languages.sparql.name();
        return loadQuery(path);
    }
}
