package org.example.rules;

import org.example.util.Target;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractDisallowRule extends AbstractMethodDetection {

    private Set<Target> targets;
    private Set<Target> exclusions;

    protected abstract Set<Target> buildTargets();

    protected abstract Set<Target> buildExclusions();

    protected Set<Target> getTargets() {
        if (targets == null) {
            targets = buildTargets();
        }

        return targets;
    }

    protected Set<Target> getExclusions() {
        if (exclusions == null) {
            exclusions = buildExclusions();
        }

        return exclusions;
    }

    protected boolean inExclusions(MethodInvocationTree mit) {
        if (getExclusions().isEmpty()) {
            return false;
        }

        Optional<String> enclosingClassName = getEnclosingClassName(mit);
        Optional<String> enclosingMethodName = getEnclosingMethodName(mit);

        Set<String> exclusionClasses = getExclusions().stream()
                .map(Target::getClassName)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());

        Set<String> exclusionMethods = getExclusions().stream()
                .flatMap(t -> t.getMethods().stream())
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());

        boolean checkExclusionClass = exclusionClasses.stream()
                // TODO: refactor
                .anyMatch(cn -> enclosingClassName.map(cn::equals).orElse(false));

        boolean checkExclusionMethod = exclusionMethods.stream()
                // TODO: refactor
                .anyMatch(m -> enclosingMethodName.map(m::equals).orElse(false));

        if (exclusionClasses.isEmpty()) {
            return checkExclusionMethod;
        }

        if (exclusionMethods.isEmpty()) {
            return checkExclusionClass;
        }

        return checkExclusionClass && checkExclusionMethod;
    }

    private Optional<String> getEnclosingClassName(MethodInvocationTree mit) {
        Tree parent = mit;
        while (parent != null && !parent.is(Tree.Kind.CLASS)) {
            parent = parent.parent();
        }

        return Optional.ofNullable(parent).map(p -> (ClassTree) p)
                .map(ClassTree::symbol)
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
