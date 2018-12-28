package net.n2oapp.framework.api.metadata.persister;

import org.jdom.Namespace;

/**
 * User: iryabov
 * Date: 03.12.13
 * Time: 13:12
 */
public abstract class AbstractSimpleElementPersister<E> implements ElementPersister<E> {

    private Namespace namespace;

    protected AbstractSimpleElementPersister(Namespace namespace) {
        this.namespace = namespace;
    }


    public Namespace getNamespace() {
        return namespace;
    }
}
