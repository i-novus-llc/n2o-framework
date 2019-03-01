package net.n2oapp.framework.api.metadata.validation.exception;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка валидации метаданных
 */
public class N2oMetadataValidationException extends N2oException {

    public N2oMetadataValidationException(String message) {
        super(message);
    }

    public N2oMetadataValidationException(String message, Exception e) {
        super(message, e);
    }
}
