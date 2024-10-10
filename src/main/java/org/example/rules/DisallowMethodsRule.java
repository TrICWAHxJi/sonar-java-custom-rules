package org.example.rules;

import lombok.Setter;
import org.example.util.CommonUtils;
import org.example.util.PropertyParsers;
import org.example.util.Target;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;

import java.util.Set;

@Rule(key = "DisallowMethodsRule")
public class DisallowMethodsRule extends AbstractDisallowRule {

    @Setter
    @RuleProperty(key = "targets", description = "Classes and methods, format: 'org.example.Class#method,method1,method2;...'")
    private String targetsProperty = "";

    @Setter
    @RuleProperty(key = "exclusions", description = "Classes and methods in which exclude this rule, format: 'org.example.Class#method,method1,method2;...'")
    private String exclusionsProperty = "";

    @Override
    protected MethodMatchers getMethodInvocationMatchers() {
        if (getTargets().isEmpty()) {
            return MethodMatchers.none();
        }

        MethodMatchers.TypeBuilder typeBuilder = MethodMatchers.create();
        MethodMatchers.NameBuilder nameBuilder;
        if (getTargets().isEmpty()) {
            nameBuilder = typeBuilder.ofAnyType();
        } else {
            nameBuilder = typeBuilder.ofTypes(getTargets().stream().map(Target::getClassName).toArray(String[]::new));
        }
        MethodMatchers.ParametersBuilder parametersBuilder = nameBuilder.names(getTargets().stream().flatMap(t -> t.getMethods().stream()).toArray(String[]::new));

        return parametersBuilder.withAnyParameters().build();
    }

    // TODO: add constructors and method references, see super class
    @Override
    protected void onMethodInvocationFound(MethodInvocationTree mit) {
        if (inExclusions(mit)) {
            return;
        }

        reportIssue(CommonUtils.methodName(mit), "Remove this forbidden call");
    }

    @Override
    protected Set<Target> buildTargets() {
        return PropertyParsers.parseTargets(targetsProperty);
    }

    @Override
    protected Set<Target> buildExclusions() {
        return PropertyParsers.parseTargets(exclusionsProperty);
    }
}
