package org.example.rule;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(key = "DisallowMethodsRule")
public class DisallowMethodsRule extends IssuableSubscriptionVisitor {

    @RuleProperty(key = "types", description = "Fully qualified names of the classes whose methods is forbidden")
    private String typesProperty = "";
    private final Set<String> types = new HashSet<>();

    @RuleProperty(key = "methods", description = "Name of the forbidden methods")
    private String methodsProperty = "";
    private final Set<String> methods = new HashSet<>();

    public void setTypesProperty(String typesProperty) {
        this.typesProperty = typesProperty;
    }

    public void setMethodsProperty(String methodsProperty) {
        this.methodsProperty = methodsProperty;
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.METHOD_INVOCATION, Tree.Kind.NEW_CLASS, Tree.Kind.METHOD_REFERENCE);
    }

    @Override
    public void visitNode(Tree tree) {
        if (types.isEmpty()) {
            types.addAll(split(typesProperty));
        }

        if (methods.isEmpty()) {
            methods.addAll(split(methodsProperty));
        }

        // TODO: add method references and constructors
        if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
            MethodInvocationTree mit = (MethodInvocationTree) tree;
            if (check(mit)) {
                onMethodInvocationFound(mit);
            }
        }
    }

    protected void onMethodInvocationFound(MethodInvocationTree mit) {
        reportIssue(mit, "Remove this forbidden call");
    }

    private Set<String> split(String property) {
        return Arrays.stream(property.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    // TODO: add check for superclasses
    private boolean check(MethodInvocationTree mit) {
        if (mit.methodSelect().is(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree mset = (MemberSelectExpressionTree) mit.methodSelect();
            String type = mset.expression().symbolType().fullyQualifiedName();

            String method = mit.methodSymbol().name();

            if (types.isEmpty()) {
                return methods.contains(method);
            }

            return types.contains(type) && methods.contains(method);
        };

        // TODO: More tests on this
        return false;
    }
}
