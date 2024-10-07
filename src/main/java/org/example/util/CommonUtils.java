package org.example.util;

import lombok.experimental.UtilityClass;
import org.sonar.plugins.java.api.tree.*;

@UtilityClass
public class CommonUtils {

    public static IdentifierTree methodName(MethodInvocationTree mit) {
        ExpressionTree methodSelect = mit.methodSelect();
        IdentifierTree id;
        if (methodSelect.is(Tree.Kind.IDENTIFIER)) {
            id = (IdentifierTree) methodSelect;
        } else {
            id = ((MemberSelectExpressionTree) methodSelect).identifier();
        }
        return id;
    }
}
