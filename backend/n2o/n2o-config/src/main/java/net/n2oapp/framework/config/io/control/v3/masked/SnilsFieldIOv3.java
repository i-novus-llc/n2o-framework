package net.n2oapp.framework.config.io.control.v3.masked;

import net.n2oapp.framework.api.metadata.control.masked.N2oSnilsField;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись поля {@code <snils>} версии 3.0
 */
@Component
public class SnilsFieldIOv3 extends MaskedFieldIOv3<N2oSnilsField> {

    @Override
    public Class<N2oSnilsField> getElementClass() {
        return N2oSnilsField.class;
    }

    @Override
    public String getElementName() {
        return "snils";
    }
}
