package org.thesis.graphQT.cypherToGremlin;

import org.junit.Test;
import org.opencypher.gremlin.translation.CypherAst;
import org.opencypher.gremlin.translation.groovy.GroovyPredicate;
import org.opencypher.gremlin.translation.translator.Translator;
import org.opencypher.gremlin.translation.translator.TranslatorFlavor;
import org.thesis.graphQT.ResourceHelper;

import java.io.IOException;

public class CypherToGremlinTranslation {


    private final String prefix = "query";
    private final int totalQuery = 11;

    @Test
    public void query4() throws IOException {
        int number = 4;
        String gremlin = translateToGremlin(number);
        System.out.println(gremlin);
    }

    @Test
    public void customQueryTranslation() throws IOException {
        String query = "MATCH (n{productID:343}) RETURN n.ProductPropertyNumeric_1 UNION MATCH (n{productID:350}) RETURN n.ProductPropertyNumeric_1";
        String gremlin = translateToGremlin(query);
        System.out.println(gremlin);
    }

    @Test
    public void calculateTranslationTime() throws Exception {
        long startTime, endTime;
        double totalTimePerQuery = 0;
        for (int qID = 1; qID < totalQuery + 1; qID++) {
            //for (int qID = totalQuery; qID > 0; qID--) {
            final String inputQuery = ResourceHelper.loadBSBMCypherQuery(prefix, qID);
            String outputQuery = "";
            for (int i = 0; i < 10; i++) {
                startTime = System.nanoTime();
                outputQuery = translateToGremlin(inputQuery);
                endTime = System.nanoTime();
                final double total = (endTime - startTime) / 1e6;
                totalTimePerQuery += total;
            }
            System.out.println(qID + ": " + totalTimePerQuery / 10);
            System.out.println("------------------");
            totalTimePerQuery = 0;
        }
    }

    private String translateToGremlin(int number) throws IOException {
        final String inputQuery = ResourceHelper.loadBSBMCypherQuery(prefix, number);
        return translateToGremlin(inputQuery);
    }

    private String translateToGremlin(String inputQuery) {
        CypherAst ast = CypherAst.parse(inputQuery);
        //System.out.println(ast.toString());
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy()
                .enableCypherExtensions().build(TranslatorFlavor.gremlinServer());
        return ast.buildTranslation(translator);
    }
}
