package net.n2oapp.framework.config.io.control.v3.masked;

import net.n2oapp.framework.api.metadata.control.masked.N2oEmailField;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись поля {@code <email>} версии 3.0
 */
@Component
public class EmailFieldIOv3 extends MaskedFieldIOv3<N2oEmailField> {

    @Override
    public Class<N2oEmailField> getElementClass() {
        return N2oEmailField.class;
    }

    @Override
    public String getElementName() {
        return "email";
    }
}
