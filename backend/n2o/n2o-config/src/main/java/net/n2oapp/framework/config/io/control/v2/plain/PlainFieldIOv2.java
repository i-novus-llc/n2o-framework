package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v2.StandardFieldIOv2;
import org.jdom2.Element;

/**
 * Чтение/запись базовых свойств простого поля
 */
public abstract class PlainFieldIOv2<T extends N2oPlainField> extends StandardFieldIOv2<T> {
    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "default-value", m::getDefaultValue, m::setDefaultValue);
    }
}
