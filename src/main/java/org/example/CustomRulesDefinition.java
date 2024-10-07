package org.example;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public class CustomRulesDefinition implements RulesDefinition {

    // don't change that because the path is hard coded in CheckVerifier
    private static final String RESOURCE_BASE_PATH = "org/sonar/l10n/java/rules/java";

    // TODO: fix this
    public static final String REPOSITORY_KEY = "org.example";
    public static final String REPOSITORY_NAME = "Example Custom Repository";

    // Add the rule keys of the rules which need to be considered as template-rules
    private static final Set<String> RULE_TEMPLATES_KEY = Set.of("DisallowMethodsRule", "DisallowInStringArgumentsRule");

    private final SonarRuntime runtime;

    public CustomRulesDefinition(SonarRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPOSITORY_KEY, "java").setName(REPOSITORY_NAME);

        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, runtime);

        ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesProvider.getChecks()));

        setTemplates(repository);

        repository.done();
    }

    private static void setTemplates(NewRepository repository) {
        RULE_TEMPLATES_KEY.stream().map(repository::rule).filter(Objects::nonNull).forEach(rule -> rule.setTemplate(true));
    }
}
