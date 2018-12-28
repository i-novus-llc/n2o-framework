package net.n2oapp.framework.api.metadata.persister;

import org.jdom.Namespace;
import net.n2oapp.engine.factory.EngineFactory;

/**
 * User: iryabov
 * Date: 03.12.13
 * Time: 13:12
 */
public abstract class AbstractElementPersister<E> implements ElementPersister<E> {
    private ElementPersisterFactory persisterFactory;
    private Namespace namespace;

    protected AbstractElementPersister() {
    }

    protected AbstractElementPersister(
            ElementPersisterFactory persisterFactory, Namespace namespace) {
        this.persisterFactory = persisterFactory;
        this.namespace = namespace;
    }

    public ElementPersisterFactory getPersisterFactory() {
        return persisterFactory;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setPersisterFactory(ElementPersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }
}
