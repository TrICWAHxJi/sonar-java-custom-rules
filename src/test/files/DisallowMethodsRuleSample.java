import java.util.List;

class CustomCheckSample {

    public void foo() {
        Inner inner = new Inner();
        inner.someMethod();
        List.of(); // Noncompliant {{Remove this forbidden call}}
    }

    public void bar() {
        //JavaScriptExecutor.click("button");
        "".trim(); // Noncompliant {{Remove this forbidden call}}
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