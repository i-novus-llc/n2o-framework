package net.n2oapp.framework.config.io.control.v3.masked;

import net.n2oapp.framework.api.metadata.control.masked.N2oPhoneField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись поля {@code <phone>} версии 3.0
 */
@Component
public class PhoneFieldIOv3 extends MaskedFieldIOv3<N2oPhoneField>{

    @Override
    public void io(Element e, N2oPhoneField m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeArray(e, "country", ",",  m::getCountry, m::setCountry);
    }

    @Override
    public Class<N2oPhoneField> getElementClass() {
        return N2oPhoneField.class;
    }

    @Override
    public String getElementName() {
        return "phone";
    }
}
