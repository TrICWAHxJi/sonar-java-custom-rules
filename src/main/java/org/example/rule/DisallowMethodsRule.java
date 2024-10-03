package org.example.rule;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(key = "DisallowMethodsRule")
public class DisallowMethodsRule extends AbstractMethodDetection {

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
    protected MethodMatchers getMethodInvocationMatchers() {
        if (types.isEmpty()) {
            types.addAll(split(typesProperty));
        }

        if (methods.isEmpty()) {
            methods.addAll(split(methodsProperty));
        }

        if (methods.isEmpty()) {
            return MethodMatchers.none();
        }
        MethodMatchers.TypeBuilder typeBuilder = MethodMatchers.create();
        MethodMatchers.NameBuilder nameBuilder;
        if (!types.isEmpty()) {
            nameBuilder = typeBuilder.ofTypes(types.toArray(new String[0]));
        } else {
            nameBuilder = typeBuilder.ofAnyType();
        }
        MethodMatchers.ParametersBuilder parametersBuilder = nameBuilder.names(methods.toArray(new String[0]));

        return parametersBuilder.withAnyParameters().build();
    }

    protected void onMethodInvocationFound(MethodInvocationTree mit) {
        reportIssue(methodName(mit), "Remove this forbidden call");
    }

    private Set<String> split(String property) {
        return Arrays.stream(property.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
