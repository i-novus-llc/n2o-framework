package net.n2oapp.framework.config.persister.invocation;

import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;

/**
 * User: iryabov
 * Date: 21.12.13
 * Time: 14:48
 */
public abstract class N2oInvocationPersister<E extends N2oInvocation> extends AbstractN2oMetadataPersister<E> {

    public N2oInvocationPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0", "n2o");
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    @Override
    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }
}
