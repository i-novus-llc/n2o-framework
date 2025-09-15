package net.n2oapp.framework.config.io.control.v3.masked;

import net.n2oapp.framework.api.metadata.control.masked.N2oUuidField;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись поля {@code <uuid>} версии 3.0
 */
@Component
public class UuidFieldIOv3 extends MaskedFieldIOv3<N2oUuidField> {

    @Override
    public Class<N2oUuidField> getElementClass() {
        return N2oUuidField.class;
    }

    @Override
    public String getElementName() {
        return "uuid";
    }
}
