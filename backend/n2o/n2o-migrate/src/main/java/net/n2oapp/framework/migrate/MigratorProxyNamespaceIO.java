package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class MigratorProxyNamespaceIO<T extends NamespaceUriAware> extends ProxyNamespaceIO<T> {

    public MigratorProxyNamespaceIO(NamespaceIO<T> io) {
        super(io);
    }

    @Override
    public Element persist(T entity, Namespace namespace) {
        Element element = new Element(getElementName(), entity.getNamespaceUri() != null ? entity.getNamespaceUri() : namespace.getURI());
        MigratorInfoHolder.pushCurrentSource(entity);
        if (entity instanceof N2oMetadata m) {
            getProcessor().additionalNamespaces(element, m::getAdditionalNamespaces, m::setAdditionalNamespaces);
        }
        getIo().io(element, entity, getProcessor());
        MigratorInfoHolder.popCurrentSource();
        return element;
    }

    @Override
    public T read(Element element) {
        T entity = newInstance(element);
        MigratorInfoHolder.pushCurrentSource(entity);
        if (entity instanceof N2oMetadata m) {
            getProcessor().additionalNamespaces(element, m::getAdditionalNamespaces, m::setAdditionalNamespaces);
        }
        getIo().io(element, entity, getProcessor());
        entity.setNamespaceUri(element.getNamespaceURI());
        entity.setNamespacePrefix(element.getNamespacePrefix());
        MigratorInfoHolder.popCurrentSource();
        return entity;
    }
}