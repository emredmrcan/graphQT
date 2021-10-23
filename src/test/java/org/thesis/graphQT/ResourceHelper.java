package org.thesis.graphQT;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceHelper {

    public static String loadSparqlQuery(final String prefix, final int number) throws IOException {
        return loadQuery(prefix, number, Languages.sparql);
    }

    public static String loadCypherQuery(final String prefix, final int number) throws IOException {
        return loadQuery(prefix,number, Languages.cypher);
    }

    public static String loadQuery(final String prefix, final int number, final Languages lang) throws IOException {
        final String path = "/queries/" + lang.name() + "/" + prefix + number + "." + lang.name();
        final InputStream stream = ResourceHelper.class.getResourceAsStream(path);
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

}
