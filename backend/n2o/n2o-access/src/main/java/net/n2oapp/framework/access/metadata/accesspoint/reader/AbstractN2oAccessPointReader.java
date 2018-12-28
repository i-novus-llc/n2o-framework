package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

/**
 * Абстрактная реализация прав доступа
 */
public abstract class AbstractN2oAccessPointReader<E extends AccessPoint> implements N2oAccessReader<E> {

    private String namespaceUri = "http://n2oapp.net/framework/config/schema/n2o-access-point-1.0";

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }
}
