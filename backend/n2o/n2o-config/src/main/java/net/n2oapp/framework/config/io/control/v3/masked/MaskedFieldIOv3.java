package net.n2oapp.framework.config.io.control.v3.masked;

import net.n2oapp.framework.api.metadata.control.masked.N2oMaskedField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.plain.PlainFieldIOv3;
import org.jdom2.Element;

/**
 * Чтение/запись поля ввода с маской версии 3.0
 */
public abstract class MaskedFieldIOv3<T extends N2oMaskedField> extends PlainFieldIOv3<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "invalid-text", m::getInvalidText, m::setInvalidText);
    }
}
