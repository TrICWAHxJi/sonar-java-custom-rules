package org.example.rule;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class DisallowMethodsRuleTest {

    @Test
    void testHasNoIssues() {
        var check = new DisallowMethodsRule();
        check.setTypesProperty("java.util.List");
        check.setMethodsProperty("of1");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowMethodsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }

    @Test
    void testHasIssues() {
        var check = new DisallowMethodsRule();
        check.setTypesProperty("java.lang.String, java.util.List");
        check.setMethodsProperty("trim,of");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowMethodsRuleSample.java")
                .withCheck(check)
                .verifyIssues();
    }

}