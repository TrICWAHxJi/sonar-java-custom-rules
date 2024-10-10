package org.example.rules;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class DisallowInStringArgumentsRuleTest {

    @Test
    void testHasNoIssues() {
        var check = new DisallowInStringArgumentsRule();
        check.setTargetsProperty("java.util.List#of1");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowInStringArgumentsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }

    @Test
    void testHasIssues() {
        var check = new DisallowInStringArgumentsRule();
        check.setTargetsProperty("java.util.Set#of");
        check.setForbiddenString("test");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowInStringArgumentsRuleSample.java")
                .withCheck(check)
                .verifyIssues();
    }

    @Test
    void testExclusions() {
        var check = new DisallowInStringArgumentsRule();
        check.setTargetsProperty("java.util.Set#of");
        check.setForbiddenString("test");

        check.setExclusionsProperty("org.example.CustomCheckSample#bar");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowInStringArgumentsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }

    @Test
    void shouldExcludeOnlyByMethod() {
        var check = new DisallowInStringArgumentsRule();
        check.setTargetsProperty("java.util.Set#of");
        check.setForbiddenString("test");

        check.setExclusionsProperty("#bar");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowInStringArgumentsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }

    @Test
    void shouldExcludeOnlyByClass() {
        var check = new DisallowInStringArgumentsRule();
        check.setTargetsProperty("java.util.Set#of");
        check.setForbiddenString("test");

        check.setExclusionsProperty("org.example.CustomCheckSample");

        CheckVerifier.newVerifier()
                .onFile("src/test/files/DisallowInStringArgumentsRuleSample.java")
                .withCheck(check)
                .verifyNoIssues();
    }
}