package org.example.rules;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class DisallowMethodsRuleTest {

    @Test
    void testHasNoIssues() {
        var check = new DisallowMethodsRule();
        check.setTargetsProperty("java.util.List#of1");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowMethodsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }

    @Test
    void testHasIssues() {
        var check = new DisallowMethodsRule();
        check.setTargetsProperty("java.lang.String#trim;java.util.Set#of");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowMethodsRuleSample.java")
                .withCheck(check)
                .verifyIssues();
    }

    // TODO: test exclusions        

}