package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oPassword;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента ввода пароля версии 3.0
 */
@Component
public class PasswordIOv3 extends PlainFieldIOv3<N2oPassword> {

    @Override
    public void io(Element e, N2oPassword m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "length", m::getLength, m::setLength);
        p.attributeBoolean(e, "eye", m::getEye, m::setEye);
    }

    @Override
    public Class<N2oPassword> getElementClass() {
        return N2oPassword.class;
    }

    @Override
    public String getElementName() {
        return "password";
    }
}
