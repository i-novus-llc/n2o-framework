package net.n2oapp.framework.config.io.event;

import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись базовых свойств событий
 */
public abstract class AbstractEventIO<T extends N2oAbstractEvent> implements NamespaceIO<T> {

    public static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/event-3.0");

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
    }
}
