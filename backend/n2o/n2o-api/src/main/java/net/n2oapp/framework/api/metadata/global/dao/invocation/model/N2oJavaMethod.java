package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import net.n2oapp.framework.api.metadata.global.dao.tools.N2oRmi;

/**
 * Модель действия java-method
 */
@Deprecated
public class N2oJavaMethod implements N2oArgumentsInvocation {
    private String bean;
    private String name;
    private N2oRmi rmi;
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

    public N2oRmi getRmi() {
        return rmi;
    }

    public void setRmi(N2oRmi rmi) {
        this.rmi = rmi;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public void setArguments(Argument[] arguments) {
        this.arguments = arguments;
    }

}
