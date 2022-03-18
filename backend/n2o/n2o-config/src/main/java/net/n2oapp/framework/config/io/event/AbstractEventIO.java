package net.n2oapp.framework.config.io.event;

import net.n2oapp.framework.api.metadata.application.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;

/**
 * Чтение\запись базовых свойств событий
 */
public abstract class AbstractEventIO<T extends N2oAbstractEvent> implements TypedElementIO<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
    }
}
