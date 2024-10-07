package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class PropertyParsers {

    public static <T> Set<T> parseSet(String property, String delimiter, Function<String, T> mapper) {
        return Arrays.stream(property.split(delimiter))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(mapper)
                .collect(Collectors.toSet());
    }

    /**
     * Parse string like "org.example.Class#method,method1,method2;..."
     *
     * @return {@link Target}s representing this property
     */
    public static Set<Target> parseTargets(String property) {
        return Arrays.stream(property.split(";"))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(Target::fromString)
                .collect(Collectors.toSet());
    }
}
