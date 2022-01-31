package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;

/**
 * Чтение запись базовых свойств компонентов версии 3.0
 */
public abstract class ComponentIOv3<T extends N2oComponent> implements NamespaceIO<T> {
    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }
}
