package org.example.util;

import lombok.Value;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class Target {

    String className;
    Set<String> methods;

    public static Target fromString(String s) {
        // TODO: check with regex to match exactly this type of property
        if (s.isBlank()) {
            throw new IllegalArgumentException("Empty property");
        }

        String[] parts = s.split("#");

        if (parts.length < 1) {
            throw new IllegalArgumentException("Target property must be like 'org.example.Class#method,method1,method2'");
        }

        String className;
        String methodsValue;

        // handle without class, like #method1,method2,method3
        if (parts.length == 1) {
            className = "";
            methodsValue = parts[0];
        } else {
            className = parts[0];
            methodsValue = parts[1];
        }

        Set<String> methods = Arrays.stream(methodsValue.split(","))
                .filter(str -> !str.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());

        return new Target(className, methods);
    }
}
