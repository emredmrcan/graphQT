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

    @Test
    public void query4() throws IOException {
        int number = 4;
        String gremlin = translateToGremlin(number);
        System.out.println(gremlin);
    }

    @Test
    public void customQueryTranslation() throws IOException {
        String query = "MATCH (n) WHERE n.ProductPropertyNumeric_1 = 1 and n.ProductPropertyNumeric_2 > 100 RETURN n.productID,n.ProductPropertyNumeric_1,n.ProductPropertyNumeric_2";
        String gremlin = translateToGremlin(query);
        System.out.println(gremlin);
    }

    private String translateToGremlin(int number) throws IOException {
        final String inputQuery = ResourceHelper.loadCypherQuery(prefix, number);
        return translateToGremlin(inputQuery);
    }

    private String translateToGremlin(String inputQuery) {
        CypherAst ast = CypherAst.parse(inputQuery);
        System.out.println(ast.toString());
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy()
                .enableCypherExtensions().build(TranslatorFlavor.gremlinServer());
        return ast.buildTranslation(translator);
    }
}
