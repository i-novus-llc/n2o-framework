package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.metadata.io.ProxyTypedElementIO;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class MigratorProxyTypedElementIO<T> extends ProxyTypedElementIO<T> {

    public MigratorProxyTypedElementIO(TypedElementIO<T> io) {
        super(io);
    }

    @Override
    public Element persist(T entity, Namespace namespace) {
        MigratorInfoHolder.pushCurrentSource(entity);
        Element element = super.persist(entity, namespace);
        MigratorInfoHolder.popCurrentSource();
        return element;
    }

    @Override
    public T read(Element element) {
        T entity = newInstance(element);
        MigratorInfoHolder.pushCurrentSource(entity);
        getIo().io(element, entity, getProcessor());
        MigratorInfoHolder.popCurrentSource();
        return entity;
    }
}