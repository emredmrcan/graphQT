package org.thesis.graphQT.gremlin;

import org.junit.Test;
import org.opencypher.gremlin.translation.CypherAst;
import org.opencypher.gremlin.translation.groovy.GroovyPredicate;
import org.opencypher.gremlin.translation.translator.Translator;
import org.opencypher.gremlin.translation.translator.TranslatorFlavor;
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

    @Test
    public void query2() throws IOException {
        //String q = "MATCH (b:Boat)-[:SAILS_TO]->(:Location {name: 'Denmark'}) RETURN b";
        String q = "CREATE (src:character {name:'saturn', age: 10000, type:'titan'})";
        CypherAst ast = CypherAst.parse(q);
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy().build();
        System.out.println(ast.buildTranslation(translator));
    }

    @Test
    public void insertVertex() throws IOException {
        System.out.println("Insert Vertex");
        String q = "CREATE (u:University {name:'Ege University', abbreviation: 'EGE'})";
        translateAndPrint(q);

        System.out.println("Insert Edge");
        q = "CREATE (u1)-[rel:located_in]->(c2)";
        translateAndPrint(q);

        System.out.println("Update");
        q = "MATCH (u:University {abbreviation:'UNIGE'}) SET u.name = 'University of Genova'";
        translateAndPrint(q);

        System.out.println("Fetch/Reading");
        q = "MATCH (c:Country {name:'ITALY'}) RETURN properties(c)";
        translateAndPrint(q);

        System.out.println("Finding the university names located in ITALY, ordered by name");
        q = "MATCH (u:University)-[:located_in]->(c:Country {name:'ITALY'}) RETURN u.name ORDER BY u.name";
        translateAndPrint(q);

        System.out.println("Finding countries that are more than 2000 kilometers apart");
        q = "MATCH (c1:Country)-[con:connection]->(c2:Country) WHERE con.distance > 2000 RETURN c1,c2";
        translateAndPrint(q);

    }

    private void translateAndPrint(String query) {
        System.out.println(query);
        CypherAst ast = CypherAst.parse(query);
        Translator<String, GroovyPredicate> translator = Translator.builder()
                .gremlinGroovy()
                .enableCypherExtensions()
                .build();
        System.out.println(ast.buildTranslation(translator));
        System.out.println("-----------------------------------------------------------");
    }

    private String translateToGremlin(int number) throws IOException {
        CypherAst ast = CypherAst.parse(ResourceHelper.loadCypherQuery(prefix, number));
        System.out.println(ast.toString());
        Translator<String, GroovyPredicate> translator = Translator.builder().gremlinGroovy().enableCypherExtensions().build(TranslatorFlavor.gremlinServer());
        return ast.buildTranslation(translator);
    }
}
