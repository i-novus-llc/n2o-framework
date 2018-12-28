package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;

/**
 * @author rgalina
 * @since 13.01.2016
 */
public abstract class N2oAccessPointPersister<E extends AccessPoint> extends AbstractN2oMetadataPersister<E> {
    public N2oAccessPointPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-access-point-1.0", "n2o");
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
