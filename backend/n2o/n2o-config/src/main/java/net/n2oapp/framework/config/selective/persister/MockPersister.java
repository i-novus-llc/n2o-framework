package net.n2oapp.framework.config.selective.persister;

import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author operehod
 * @since 24.04.2015
 */
public class MockPersister extends AbstractN2oMetadataPersister {

    public MockPersister() {
        super("", "");
    }

    @Override
    public void setNamespaceUri(String uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNamespacePrefix(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element persist(Object entity, Namespace namespace) {
        return new MockElement();
    }

    @Override
    public Class getElementClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getElementName() {
        return "";
    }
}
