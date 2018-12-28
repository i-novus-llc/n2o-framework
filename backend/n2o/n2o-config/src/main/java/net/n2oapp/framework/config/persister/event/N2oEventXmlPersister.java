package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;

/**
 * @author rgalina
 * @since 31.03.2016
 */
public abstract class N2oEventXmlPersister<T extends N2oAction> extends AbstractN2oMetadataPersister<T> {
    public N2oEventXmlPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-event-1.0", "evt");
    }

    @Override
    public void setNamespaceUri(String uri) {
        this.namespaceUri = uri;
    }

    @Override
    public void setNamespacePrefix(String prefix) {
        this.namespacePrefix = prefix;
    }
}
