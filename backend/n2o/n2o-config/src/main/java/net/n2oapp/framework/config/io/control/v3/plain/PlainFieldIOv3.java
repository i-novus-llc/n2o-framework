package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.StandardFieldIOv3;
import org.jdom2.Element;

/**
 * Чтение/запись базовых свойств простого поля версии 3.0
 */
public abstract class PlainFieldIOv3<T extends N2oPlainField> extends StandardFieldIOv3<T> {
    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "default-value", m::getDefaultValue, m::setDefaultValue);
    }
}
