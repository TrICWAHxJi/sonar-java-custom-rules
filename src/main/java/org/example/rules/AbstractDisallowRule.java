package org.example.rules;

import org.example.util.Target;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractDisallowRule extends AbstractMethodDetection {

    // TODO: make private
    protected Set<Target> targets;
    protected Set<Target> exclusions;

    protected abstract Set<Target> targets();

    protected abstract Set<Target> exclusions();

    protected boolean inExclusions(MethodInvocationTree mit) {
        if (exclusions().isEmpty()) {
            return false;
        }

        Optional<String> className = getClassName(mit);
        Optional<String> enclosingMethodName = getEnclosingMethodName(mit);

        boolean checkClass = exclusions().stream()
                .map(Target::getClassName)
                .filter(s -> !s.isBlank())
                // TODO: refactor
                .anyMatch(cn -> className.map(cn::equals).orElse(false));

        boolean checkExclusionMethod = exclusions().stream()
                .flatMap(t -> t.getMethods().stream())
                .filter(s -> !s.isBlank())
                // TODO: refactor
                .anyMatch(m -> enclosingMethodName.map(m::equals).orElse(false));

        // TODO: if not classes, check only methods and if no methods check only class
        return checkClass && checkExclusionMethod;
    }

    private Optional<String> getClassName(MethodInvocationTree mit) {
        return Optional.ofNullable(mit)
                .map(MethodInvocationTree::methodSymbol)
                .map(Symbol::owner)
                .map(Symbol::type)
                .map(Type::fullyQualifiedName);
    }

    private Optional<String> getEnclosingMethodName(MethodInvocationTree mit) {
        Tree parent = mit;
        while (parent != null && !parent.is(Tree.Kind.METHOD)) {
            parent = parent.parent();
        }

        return Optional.ofNullable(parent).filter(p -> p.is(Tree.Kind.METHOD))
                .map(p -> (MethodTree) p)
                .map(MethodTree::symbol)
                .map(Symbol::name);
    }
}
