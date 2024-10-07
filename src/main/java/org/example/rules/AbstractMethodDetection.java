package org.example.rules;

import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.tree.*;

import java.util.*;

public abstract class AbstractMethodDetection extends IssuableSubscriptionVisitor {

    private MethodMatchers matchers;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.METHOD_INVOCATION, Tree.Kind.NEW_CLASS, Tree.Kind.METHOD_REFERENCE);
    }

    @Override
    public void visitNode(Tree tree) {
        if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
            MethodInvocationTree mit = (MethodInvocationTree) tree;
            if (matchers().matches(mit)) {
                onMethodInvocationFound(mit);
            }
        } else if (tree.is(Tree.Kind.NEW_CLASS)) {
            NewClassTree newClassTree = (NewClassTree) tree;
            if (matchers().matches(newClassTree)) {
                onConstructorFound(newClassTree);
            }
        } else if (tree.is(Tree.Kind.METHOD_REFERENCE)) {
            MethodReferenceTree methodReferenceTree = (MethodReferenceTree) tree;
            if (matchers().matches(methodReferenceTree)) {
                onMethodReferenceFound(methodReferenceTree);
            }
        }
    }

    protected abstract MethodMatchers getMethodInvocationMatchers();

    protected void onMethodInvocationFound(MethodInvocationTree mit) {
        // Do nothing by default
    }

    protected void onConstructorFound(NewClassTree newClassTree) {
        // Do nothing by default
    }

    protected void onMethodReferenceFound(MethodReferenceTree methodReferenceTree) {
        // Do nothing by default
    }

    protected MethodMatchers matchers() {
        if (matchers == null) {
            matchers = getMethodInvocationMatchers();
        }
        return matchers;
    }
}
