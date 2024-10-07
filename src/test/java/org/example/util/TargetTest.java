package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class TargetTest {

    @Test
    void fromString() {
        String property = "com.example.Class#someMethod,anotherMethod,test";

        var expectedTarget = new Target("com.example.Class", Set.of("someMethod", "anotherMethod", "test"));
        var actualTarget = Target.fromString(property);

        assertEquals(expectedTarget, actualTarget);
    }
}