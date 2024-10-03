package org.example;

import org.sonar.api.Plugin;

public class CustomRulesPlugin implements Plugin {

    @Override
    public void define(Context context) {
        // server extensions -> objects are instantiated during server startup
        context.addExtension(CustomRulesDefinition.class);

        // batch extensions -> objects are instantiated during code analysis
        context.addExtension(CustomRulesRegistrar.class);
    }
}
