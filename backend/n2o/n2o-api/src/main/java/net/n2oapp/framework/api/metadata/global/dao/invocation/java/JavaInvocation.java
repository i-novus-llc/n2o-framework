package net.n2oapp.framework.api.metadata.global.dao.invocation.java;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;

/**
 * Параметры для вызова java методов
 */
@Deprecated
public class JavaInvocation implements N2oArgumentsInvocation {

    private String className;
    private String methodName;
    private Argument[] arguments;
    private String namespaceUri;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public void setArguments(Argument[] arguments) {
        this.arguments = arguments;
    }

}

