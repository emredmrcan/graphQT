/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.thesis.graphQT.gremlin;

import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.expr.*;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

import java.util.List;

class WhereTraversalBuilder {

    public static GraphTraversal<?, ?> transform(final E_Equals expression) {
    //	System.out.println("The aggr one : "+expression.getArg1().getClass().getName() + "The aggr one :"+expression.getArg2().getClass().getName());
        final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.eq(value));
    }

    public static GraphTraversal<?, ?> transform(final E_NotEquals expression) {
        final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.neq(value));
    }

    public static GraphTraversal<?, ?> transform(final E_LessThan expression) {
    //	System.out.println("The aggr one : "+expression.getArg1().getClass().getName() + "The aggr one :"+expression.getArg2().getClass().getName());
    	final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.lt(value));
    }

    public static GraphTraversal<?, ?> transform(final E_LessThanOrEqual expression) {
    //	System.out.println("The aggr one : "+expression.getArg1().getClass().getName() + "The aggr one :"+expression.getArg2().getClass().getName());
        final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.lte(value));
    }

    public static GraphTraversal<?, ?> transform(final E_GreaterThan expression) {
        final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.gt(value));
    }

    public static GraphTraversal<?, ?> transform(final E_GreaterThanOrEqual expression) {
        final Object value = expression.getArg2().getConstant().getNode().getLiteralValue();
        return __.as(expression.getArg1().getVarName()).is(P.gte(value));
    }

    public static GraphTraversal<?, ?> transform(final E_LogicalAnd expression) {
        return __.and(
                transform(expression.getArg1()),
                transform(expression.getArg2()));
    }

    public static GraphTraversal<?, ?> transform(final E_LogicalOr expression) {
        return __.or(
                transform(expression.getArg1()),
                transform(expression.getArg2()));
    }

    public static GraphTraversal<?, ?> transform(final E_Exists expression) {
        final OpBGP opBGP = (OpBGP) expression.getGraphPattern();
        final List<Triple> triples = opBGP.getPattern().getList();
        if (triples.size() != 1) throw new IllegalStateException("Unhandled EXISTS pattern");
        final GraphTraversal<?, ?> traversal = TraversalBuilder.transform(triples.get(0));
        final Step endStep = traversal.asAdmin().getEndStep();
        final String label = (String) endStep.getLabels().iterator().next();
        endStep.removeLabel(label);
        return traversal;
    }
    

    public static GraphTraversal<?, ?> transform(final E_NotExists expression) {
        final OpBGP opBGP = (OpBGP) expression.getGraphPattern();
        final List<Triple> triples = opBGP.getPattern().getList();
        if (triples.size() != 1) throw new IllegalStateException("Unhandled NOT EXISTS pattern");
        final GraphTraversal<?, ?> traversal = TraversalBuilder.transform(triples.get(0));
        final Step endStep = traversal.asAdmin().getEndStep();
        final String label = (String) endStep.getLabels().iterator().next();
        endStep.removeLabel(label);
        return __.not(traversal);
    }
    
    public static int getStrLength(final E_StrLength expression){
    	
    	return expression.getArg().toString().length();
    	
    }
    
    
    //what does <?, ?> signify? possibly <S,E>
    public static GraphTraversal<?, ?> transform(final Expr expression) {
        if (expression instanceof E_Equals) return transform((E_Equals) expression);
        if (expression instanceof E_NotEquals) return transform((E_NotEquals) expression);
        if (expression instanceof E_LessThan) return transform((E_LessThan) expression);
        if (expression instanceof E_LessThanOrEqual) return transform((E_LessThanOrEqual) expression);
        if (expression instanceof E_GreaterThan) return transform((E_GreaterThan) expression);
        if (expression instanceof E_GreaterThanOrEqual) return transform((E_GreaterThanOrEqual) expression);
        if (expression instanceof E_LogicalAnd) return transform((E_LogicalAnd) expression);
        if (expression instanceof E_LogicalOr) return transform((E_LogicalOr) expression);
        if (expression instanceof E_Exists) return transform((E_Exists) expression);
        if (expression instanceof E_NotExists) return transform((E_NotExists) expression);
        throw new IllegalStateException(String.format("Unhandled expression: %s", expression));
    }
}
