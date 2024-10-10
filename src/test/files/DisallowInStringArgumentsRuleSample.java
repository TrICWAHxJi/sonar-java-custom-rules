package org.example;

import java.util.List;
import java.util.Set;

class CustomCheckSample {

    public void foo() {
        Inner inner = new Inner();
        inner.someMethod();
        List.of();
        Set.of();
    }

    public void bar() {
        Set.of("test"); // Noncompliant {{Remove this forbidden call}}
        "".trim();
    }

    private int baz() {
        //AnotherClass.someMethod(1);
        return 0;
    }

    void good() {
        System.out.println("Hello, World!");
    }

    private static class Inner {

        public int someMethod() {
            return 0;
        }
    }
}