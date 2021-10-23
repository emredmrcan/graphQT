package org.thesis.graphQT.gremlin;

import org.junit.Test;
import org.opencypher.gremlin.translation.CypherAst;
import org.opencypher.gremlin.translation.groovy.GroovyPredicate;
import org.opencypher.gremlin.translation.translator.Translator;
import org.thesis.graphQT.ResourceHelper;

import java.io.IOException;

public class CypherToGremlinTest {

    private final String prefix = "query";

    @Test
    public void query4() throws IOException {
        int number = 4;
        String gremlin = translateToGremlin(number);
        System.out.println(gremlin);
    }

    private String translateToGremlin(int number) throws IOException {
        CypherAst ast = CypherAst.parse(ResourceHelper.loadCypherQuery(prefix, number));
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy().build();
        return ast.buildTranslation(translator);
    }
}
