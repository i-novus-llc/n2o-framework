package net.n2oapp.framework.config.selective.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author operehod
 * @since 24.04.2015
 */
public class MockReader extends AbstractFactoredReader {
    @Override
    public NamespaceUriAware read(Element element, Namespace namespace) {
        return null;
    }

    @Override
    public Class getElementClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNamespaceUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getElementName() {
        throw new UnsupportedOperationException();
    }
}
