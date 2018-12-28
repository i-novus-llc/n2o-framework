package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import java.util.List;

/**
 * Модель операции с заданными заранее значениями
 */
@Deprecated
public class N2oOptionsInvocation implements N2oInvocation {

    List<String> options;
    private String namespaceUri;

    public N2oOptionsInvocation() {
    }

    public N2oOptionsInvocation(List<String> options) {
        this.options = options;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
