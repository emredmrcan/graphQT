package org.thesis.graphQT.gremlin;

import org.junit.Test;
import org.opencypher.gremlin.translation.CypherAst;
import org.opencypher.gremlin.translation.groovy.GroovyPredicate;
import org.opencypher.gremlin.translation.translator.Translator;
import org.thesis.graphQT.ResourceHelper;

import java.io.IOException;

public class CypherToGremlinTest {

    @Test
    public void thesisQuery1() throws IOException {
        CypherAst ast = CypherAst.parse(ResourceHelper.loadCypherQuery("thesis",1));
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy().build();
        String gremlin = ast.buildTranslation(translator);
        System.out.println(gremlin);
    }
}
